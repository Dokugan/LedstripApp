package tk.stoin.ledstripremote

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class ToggleService : TileService(){
    override fun onTileAdded() {
        super.onTileAdded()

        qsTile.state = Tile.STATE_INACTIVE

        qsTile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()

        val handler = HttpHandler(applicationContext)
        handler.checkState({
            println(it.toString())
            if (it == true){
                qsTile.state = Tile.STATE_ACTIVE
                qsTile.updateTile()
            }
            else {qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()}
        },{
            qsTile.state = Tile.STATE_UNAVAILABLE
            qsTile.updateTile()
        })
    }

    override fun onClick() {
        super.onClick()

        val handler = HttpHandler(applicationContext)
        if (qsTile.state == Tile.STATE_INACTIVE) {
            handler.toggleAllControllers(true, {
                qsTile.state = Tile.STATE_ACTIVE
                qsTile.updateTile()
            }, {
                qsTile.state = Tile.STATE_UNAVAILABLE
                qsTile.updateTile()
            })

        }
        else if (qsTile.state == Tile.STATE_ACTIVE)
        {
            handler.toggleAllControllers(false, {
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.updateTile()
            },{
                qsTile.state = Tile.STATE_UNAVAILABLE
                qsTile.updateTile()
            })
        }
        else if (qsTile.state == Tile.STATE_UNAVAILABLE)
        {
            handler.checkState({
                if (it == true){
                    qsTile.state = Tile.STATE_ACTIVE
                    qsTile.updateTile()
                }
                else {qsTile.state = Tile.STATE_INACTIVE
                    qsTile.updateTile()}
            },{
                qsTile.state = Tile.STATE_UNAVAILABLE
                qsTile.updateTile()
            })
        }


    }
}