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
public class SonLauncher {
    
    private String gongFile = "ressources/730.wav";
    private InputStream inputStream;
    private AudioStream audioStream;
    
    public SonLauncher(){
        
    }

    public void playSound() 
{
  try{
    inputStream = new FileInputStream(gongFile);
    audioStream = new AudioStream(inputStream);
    AudioPlayer.player.start(audioStream);
  }
  catch (Exception e)
  { }
  
}
    
     public void stopSound() 
{
  try
  {
    AudioPlayer.player.stop(audioStream);
  }
  catch (Exception e)
  { }
}
    
}
