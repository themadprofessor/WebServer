package me.httpshandler;

/**
 * Created by root on 10/04/15.
 */
public enum MediaType {
    UKNOWN(DataType.UNKNOWN),
    HTML(DataType.TEXT), CSS(DataType.TEXT), PLAIN(DataType.TEXT), CMD(DataType.TEXT), CSV(DataType.TEXT), RTF(DataType.TEXT), XML(DataType.TEXT), VND_A(DataType.TEXT), VND_ADC(DataType.TEXT),
    JPEG(DataType.IMAGE), PNG(DataType.IMAGE), GIF(DataType.IMAGE), PJPEG(DataType.IMAGE), BMP(DataType.IMAGE), TIFF(DataType.IMAGE), SVG_XML(DataType.IMAGE), VND_DJVU(DataType.IMAGE),
    MP4(DataType.VIDEO), AVI(DataType.VIDEO), OGG(DataType.VIDEO), QUICKTIME(DataType.VIDEO), WEBM(DataType.VIDEO), X_MATROSKA(DataType.VIDEO), X_MS_WMV(DataType.VIDEO), X_FLV(DataType.VIDEO),
    JAVASCRIPT(DataType.APPLICATION), XHTML_XML(DataType.APPLICATION),
    MPEG(DataType.AUDIO), FLAC(DataType.AUDIO), OPUS(DataType.AUDIO), VORBIS(DataType.AUDIO), VNF_RN_REALAUDIO(DataType.AUDIO), VND_WAVE(DataType.AUDIO);

    public DataType dataType;
    MediaType(DataType dataType) {
        this.dataType = dataType;
    }


    public static MediaType fromString(String string) {
        string = string.replace('-', '_').replace('.', '_').replace('+', '_');
        String[] split = string.split("/");
        if (split.length == 1) {
            String name = string.split("/")[0];
            name = name.replace("jpg", "jpeg");
            for (MediaType type : values()) {
                if (type.superToString().toLowerCase().equals(name)) {
                    return type;
                }
            }
        } else {
            for (MediaType type : values()) {
                if (type.toString().toLowerCase().equals(string)) {
                    return type;
                }
            }
        }
        return PLAIN;
    }

    private String superToString() {
        return super.toString();
    }

    @Override
    public String toString() {
        return dataType.toString().toLowerCase() + '/' + super.toString().toLowerCase();
    }
}
