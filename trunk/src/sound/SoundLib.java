package sound;

import java.applet.*;
import java.net.URL;
import java.util.*;

/**
 * Klasse die alle Sounds enthaelt
 * 
 * @author steven
 */
public class SoundLib {
	static SoundLib					instance;
	Hashtable<String, AudioClip>	sounds;
	Vector<AudioClip>				loopingClips;

	/**
	 * Konstruktor der Klasse
	 */
	public SoundLib () {
		sounds = new Hashtable<String, AudioClip> ();
		loopingClips = new Vector<AudioClip> ();
	}

	/**
	 * @return Instanz der Lib bekommen
	 */
	public static SoundLib getInstance () {
		if ( instance == null ) {
			instance = new SoundLib ();
		}
		return instance;
	}

	/**
	 * Sound laden.
	 * 
	 * @param name
	 *            name des Sounds
	 * @param path
	 *            Pfad zum Sound
	 */
	public void loadSound (String name, String path) {

		if ( sounds.containsKey ( name ) ) {
			return;
		}

		URL sound_url = getClass ().getClassLoader ().getResource ( path );
		sounds.put ( name, (AudioClip) Applet.newAudioClip ( sound_url ) );
	}

	/**
	 * Sound abspielen.
	 * 
	 * @param name
	 *            Name des Sounds
	 */
	public void playSound (String name) {
		AudioClip audio = sounds.get ( name );
		audio.play ();
	}

	/**
	 * Sound in Schleife Spielen
	 * 
	 * @param name
	 *            Name des Sounds
	 */
	public void loopSound (String name) {
		AudioClip audio = sounds.get ( name );
		loopingClips.add ( audio );
		audio.loop ();
	}

	/**
	 * Sound anhalten
	 * 
	 * @param name
	 *            Name des Sounds
	 */
	public void stopSound (String name) {
		AudioClip audio = sounds.get ( name );
		audio.stop ();
	}

	/**
	 * Loop anhalten.
	 */
	public void stopLoopingSound () {
		for (AudioClip c : loopingClips) {
			c.stop ();
		}
	}
}
