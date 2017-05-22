package com.example.platestack

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.Plate
import org.platestack.api.plugin.annotation.Version

@Plate("hello_maven", "Hello Maven", Version(0,1,0,"SNAPSHOT"))
object HelloMaven : PlatePlugin()
