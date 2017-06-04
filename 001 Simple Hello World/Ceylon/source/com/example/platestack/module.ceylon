"Default documentation for module `com.example.platestack`."

native ("jvm")
module com.example.platestack "0.1.0-SNAPSHOT" {
    import java.base "8";
    import maven:com.github.platestack:"immutable-collections" "v0.1.0-alpha";
    import maven:org.jetbrains.kotlin:"kotlin-stdlib" "1.1.2-4";
    import maven:org.jetbrains.kotlin:"kotlin-stdlib-jre8" "1.1.2-4";
    import maven:org.jetbrains.kotlin:"kotlin-reflect" "1.1.2-4";
    import maven:com.github.salomonbrys.kotson:"kotson" "2.5.0";
    import maven:org.slf4j:"slf4j-api" "1.7.21";
    import maven:io.github.microutils:"kotlin-logging" "1.4.4";
    import maven:org.ow2.asm:"asm" "5.2";
    import maven:com.github.platestack.plateapi:"build" "40d807b0f0";
}
