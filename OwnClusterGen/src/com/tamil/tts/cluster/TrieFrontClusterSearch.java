
package com.tamil.tts.cluster;

import java.util.List;

import com.tamil.tts.builder.InitializeObjects;
import com.tamil.tts.constants.TamilTTSConstants;
import com.tamil.tts.model.ResultModel;
import com.tamil.tts.model.TimeDataModel;
import com.tamil.tts.utils.TamilTTSUtils;

public class TrieFrontClusterSearch 
{
	public static ResultModel searchCluster(String word, List<TimeDataModel> modelList, boolean longSearch)
	{
		TimeDataModel model1 = null;
		ResultModel resultModel = null;
		List<String> letterList = TamilTTSUtils.getLetterList(word);
		List<String> trieResult =  InitializeObjects.mapTrie.getKeySuggestions(word);
		
		if(trieResult != null && trieResult.size() >= 1)
		{
			String key = trieResult.get(0);
			model1 = InitializeObjects.clusterMap.get(key);
			
			String sentence = model1.getSentence();
			String wordsArray[] = sentence.split(" ");
			for(int x = 0; x < wordsArray.length; x++)
			{
				if(wordsArray[x].trim().equals(""))
				{
					continue;
				}
				List<String> wordArrayList = TamilTTSUtils.getLetterList(wordsArray[x]);
				if(wordsArray[x].equalsIgnoreCase(word))
				{
					int startTimeIndex = getStartTimeIndex(wordsArray, x);
					int endTimeIndex= startTimeIndex + wordArrayList.size() - 1;
					int startTime = model1.getStartTime().get(startTimeIndex);
					int endTime = model1.getEndTime().get(endTimeIndex);
					resultModel = new ResultModel();
					resultModel.setStartTime(startTime );
					resultModel.setEndTime(endTime );
					resultModel.setFileName(model1.getFileName());
					resultModel.setAudioPath(model1.getAudioPath());
					resultModel.setWord(word);
					return resultModel;
				}
				
				if(!(letterList.size() <= wordArrayList.size()))
				{
					continue;
				}
				else if(forwardMatchList(letterList, wordArrayList))
				{
					int startTimeIndex = getStartTimeIndex(wordsArray, x);
					int endTimeIndex= startTimeIndex + letterList.size() - 1;
					int startTime = model1.getStartTime().get(startTimeIndex);
					int endTime = model1.getEndTime().get(endTimeIndex);
					resultModel = new ResultModel();
					resultModel.setStartTime(startTime );
					resultModel.setEndTime(endTime );
					resultModel.setFileName(model1.getFileName());
					resultModel.setAudioPath(model1.getAudioPath());
					resultModel.setWord(word);
					return resultModel;
				}
				
			}
		}
		else
		{
			return null;
		}
		
		return resultModel;
	}
	
	public static int getStartTimeIndex(String[] wordsArray, int index)
	{
		if(index == 0)
		{
			return 0;
		}
		int counter = 0;
		for(int i = 0 ; i < index ; i++)
		{
			String word = wordsArray[i];
			if(word.equalsIgnoreCase(""))
			{
				continue;
			}
			List<String> letterList = TamilTTSUtils.getLetterList(word);
			counter += letterList.size();
		}
		return counter;
	}
	
	public static boolean forwardMatchList(List<String> list1, List<String> list2)
	{
		if(!(list1.size() <= list2.size()))
		{
			return false;
		}
		for(int i = 0; i < list1.size(); i++)
		{
			if(list1.get(i).equalsIgnoreCase(list2.get(i)))
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		
		return true;
	}
	public static int calculateDuration(String word)
	   {
			word = word.replace("_", "");
			List<String> letterList = TamilTTSUtils.getLetterList(word);
			long duration = 0;
			for(int i = 0; letterList != null && i < letterList.size(); i++)
			{
				if(TamilTTSUtils.findPresence(TamilTTSConstants.BODY_WORD, letterList.get(i)))
				{
					duration += TamilTTSConstants.BODY_SOUND;
					continue;
				}
				if (TamilTTSUtils.findPresence(TamilTTSConstants.O_word, letterList.get(i))) 
				{
					duration += TamilTTSConstants.O_sound;

					continue;
				}
				if (TamilTTSUtils.findPresence(TamilTTSConstants.AI_WORD, letterList.get(i))) 
				{
					duration += TamilTTSConstants.ai_sound;

					continue;
				}
				if (TamilTTSUtils.findPresence(TamilTTSConstants.நெடில், letterList.get(i))) 
				{
					duration += TamilTTSConstants.LONG_SOUND;

					continue;
				} 
				else if(TamilTTSUtils.findPresence(TamilTTSConstants.special, letterList.get(i)))
				{
					duration += TamilTTSConstants.special_duration;

					continue;
				}
				else 
				{
					duration += TamilTTSConstants.SHORT_SOUND;

					continue;
				}
			}
			//System.out.println("Word: "+ word);
			//System.out.println("Duration: "+ duration);
			return (int) duration;
		}
	
}
