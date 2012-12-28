package com.meditatenowWorkStress.api;

import java.util.ArrayList;

import com.meditatenowWorkStress.R;

public class MNAudioManager {
	
	private static MNAudioManager _instance;
	private ArrayList<MNAudioInfo> _audios = new ArrayList<MNAudioInfo>();

	public static MNAudioManager getInstance() {
		if(_instance == null)
			_instance = new MNAudioManager();
		return _instance;
	}
	
	public MNAudioManager() {
		addAudio(R.raw.one_work_stress_introduction, (2*60 + 15)*1000 + 500, "1. Work Stress Introduction.mp3", "Settle...");
		addAudio(R.raw.two_work_stress_instructions, (5*60 + 3)*1000 + 500, "2. Work Stress Instructions.mp3", "Focus...");
		addAudio(R.raw.three_work_stress_lesson, (1*60 + 23)*1000 + 500, "3. Work Stress Lesson.mp3", "Breathe...");
	}
	
	private MNAudioInfo addAudio(int resourceId, int duration, String name, String title) {
		MNAudioInfo info = new MNAudioInfo(resourceId, duration, name, title);
		_audios.add(info);
		return info;
	}
	
	public int getAudiosCount() {
		return _audios.size();
	}
	
	public MNAudioInfo getAudioInfo(int index) {
		return _audios.get(index);
	}
	
	public int indexOfAudio(MNAudioInfo audio) {
		return _audios.indexOf(audio);
	}
	
	private boolean isAudioAllowed(MNAudioInfo audio) {
		int index = this.indexOfAudio(audio);
		MNSettingManager sett = MNSettingManager.getInstance();
		if(index == 0 && !sett.isPlayIntro())
			return false;
		if(index == 1 && !sett.isPlayInstructions())
			return false;
		return true;
	}
	
	public MNAudioInfo getFirstAudio() {
		for(MNAudioInfo audio : _audios)
			if(this.isAudioAllowed(audio))
				return audio;
		return null;
	}
	
	public MNAudioInfo getNextAudio(MNAudioInfo prevAudio) {
		if(prevAudio == null)
			return this.getFirstAudio();
		int prevIndex = this.indexOfAudio(prevAudio);
		for(int i = prevIndex + 1; i < _audios.size(); i++)
			if(this.isAudioAllowed(_audios.get(i)))
				return _audios.get(i);
		return null;
	}
}
