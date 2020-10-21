public class CodeMasterResponse {
    private int whitePegs;
    private int coloredPegs;
    private boolean quit;

    public CodeMasterResponse() {
        this.whitePegs = 0;
        this.coloredPegs = 0;
        this.quit = false;
    }

    public CodeMasterResponse(int whitePegs, int coloredPegs) {
        this.whitePegs = whitePegs;
        this.coloredPegs = coloredPegs;
        this.quit = false;
    }

    public CodeMasterResponse(int whitePegs, int coloredPegs, boolean isQuit) {
        this.whitePegs = whitePegs;
        this.coloredPegs = coloredPegs;
        this.quit = isQuit;
    }

    public int getWhitePegs() {
        return whitePegs;
    }

    public void setWhitePegs(int whitePegs) {
        this.whitePegs = whitePegs;
    }

    public int getColoredPegs() {
        return coloredPegs;
    }

    public void setColoredPegs(int coloredPegs) {
        this.coloredPegs = coloredPegs;
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }
}