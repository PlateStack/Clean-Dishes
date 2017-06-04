"Default documentation for module `com.example.platestack`."

native ("jvm")
module com.example.platestack "0.1.0-SNAPSHOT" {
    import java.base "8";
    shared import maven:org.slf4j:"slf4j-api" "1.7.25";
    shared import maven:com.github.platestack:"plateapi" "40d807b0f0";
}
