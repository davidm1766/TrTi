package sk.listok.zssk.zssklistok.classloader;

/**
 * Slúži na udržanie informácií o sťahovaní bajtkódu.
 */
public class DexDownloadInfo {
    private byte[] dexBytes;
    private eStatus status;


    public void setDexBytes(byte[] dexBytes) {
        this.dexBytes = dexBytes;
    }

    public byte[] getDexBytes() {
        return dexBytes;
    }


    public void setStatus(eStatus status) {
        this.status = status;
    }


    public eStatus getStatus() {
        if(dexBytes == null || dexBytes.length == 0 ){
            this.status = eStatus.FAILED;
        }
        return status;
    }
}
