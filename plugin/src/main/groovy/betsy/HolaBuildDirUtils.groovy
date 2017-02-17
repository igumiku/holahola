package betsy

public class HolaBuildDirUtils{
    public static String getBuildBackupDir(String buildDirPath) {
        def buildBackupDir = new File(getBuildCacheDir(buildDirPath), "hola-backup")
        if (!buildBackupDir.exists() || !buildBackupDir.isDirectory()) {
            buildBackupDir.mkdirs()
        }
        return buildBackupDir.absolutePath
    }
}