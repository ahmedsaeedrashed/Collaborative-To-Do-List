/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Team;

/**
 *
 * @author ALMOSTAFA
 */
public class ListViewCell {
    private String textContent ;
    private String flag;
    public ListViewCell(String txt , String img)
    {
        textContent = txt;
        flag = img;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    
}
