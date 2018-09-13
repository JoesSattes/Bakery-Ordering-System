/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Sattaya Singkul
 */
public class TermOfUse {
    
    private int termId;
    private String termDescript;
    
    public TermOfUse(){}
    
    public TermOfUse(int id, String descript){
        this.termDescript = descript;
        this.termId = id;
    }

    /**
     * @return the termId
     */
    public int getTermId() {
        return termId;
    }

    /**
     * @param termId the termId to set
     */
    public void setTermId(int termId) {
        this.termId = termId;
    }

    /**
     * @return the termDescript
     */
    public String getTermDescript() {
        return termDescript;
    }

    /**
     * @param termDescript the termDescript to set
     */
    public void setTermDescript(String termDescript) {
        this.termDescript = termDescript;
    }
    
}