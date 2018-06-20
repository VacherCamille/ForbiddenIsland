/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Musique;

import java.io.FileInputStream;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author vacherca
 */
public class SonJeux {
    public SonJeux(){
        
    }

    public void playSound() 
{
  try
  {
    // get the sound file as a resource out of my jar file;
    // the sound file must be in the same directory as this class file.
    // the input stream portion of this recipe comes from a javaworld.com article.
    String gongFile = "ressources/731.wav";
    InputStream inputStream = new FileInputStream(gongFile);
    AudioStream audioStream = new AudioStream(inputStream);
    AudioPlayer.player.start(audioStream);
  }
  catch (Exception e)
  { }
}
}
