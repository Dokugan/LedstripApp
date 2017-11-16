package tk.stoin.ledstripremote

import android.content.Context
import java.io.Serializable

data class Color(var r: Byte, var g: Byte, var b: Byte) : Serializable {

    fun setColor(r: Byte, g: Byte, b: Byte, onColorChanged: (Context) -> Unit, context: Context){
        this.r = r
        this.g = g
        this.b = b
        onColorChanged(context)
    }
}

data class Pattern(var colors: ArrayList<Color>) : Serializable

data class LedControllerStatus(var id: Byte, var name: String, var numLeds: Byte,var pattern : Pattern) : Serializable{

    fun syncController(context: Context){
        val handler = HttpHandler(context)
        handler.updateController(this, {},{})
    }
}