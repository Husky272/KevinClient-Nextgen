package kevin.main

import cn.a114.skid.AudioManager
import cn.a114.skid.AudioPlayer
import kevin.utils.FileUtils
import net.minecraft.client.Minecraft
import java.io.File

class PenisAudioPlayer {
    var penisAudioFile= File(KevinClient.fileManager.soundsDir, "Penis.wav")
    var penisAudio:AudioPlayer

     init{
        if(!penisAudioFile.exists()){
            try {
                FileUtils.unpackFile(penisAudioFile,"assets/minecraft/Penis.wav")
            } catch (e: Exception) {
                Minecraft.logger.error(e)
            }
        }
         penisAudio = AudioPlayer(penisAudioFile)

     }
    fun play(){
        AudioManager().penisAudio.asyncPlay()
    }
}
