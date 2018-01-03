package com.haoyun.commons.result;

import java.io.Serializable;
import java.util.Date;

public class GiftTemplateVo implements Serializable {
	private Integer fTemplateId;

    private Integer fItemId;

    private Integer fUseMax;

    private String fCreateMemory;

    private String fPrefix;
    
    private String fStatus;
    
    private Integer fNum;
    
    private String fSalesman;
    
    private String fSectionNum;
    
    private Date fInserttime;

    private Date createdateStart;
    
    private Date createdateEnd;
    
    private static final long serialVersionUID = 1L;

    public Integer getfTemplateId() {
        return fTemplateId;
    }

    public void setfTemplateId(Integer fTemplateId) {
        this.fTemplateId = fTemplateId;
    }

    public Integer getfItemId() {
        return fItemId;
    }

    public void setfItemId(Integer fItemId) {
        this.fItemId = fItemId;
    }

    public Integer getfUseMax() {
        return fUseMax;
    }

    public void setfUseMax(Integer fUseMax) {
        this.fUseMax = fUseMax;
    }

    public String getfCreateMemory() {
        return fCreateMemory;
    }

    public String getfSectionNum() {
		return fSectionNum;
	}

	public void setfSectionNum(String fSectionNum) {
		this.fSectionNum = fSectionNum;
	}

	public void setfCreateMemory(String fCreateMemory) {
        this.fCreateMemory = fCreateMemory == null ? null : fCreateMemory.trim();
    }

    public Date getfInserttime() {
        return fInserttime;
    }

    public void setfInserttime(Date fInserttime) {
        this.fInserttime = fInserttime;
    }

    public Date getCreatedateStart() {
		return createdateStart;
	}

	public void setCreatedateStart(Date createdateStart) {
		this.createdateStart = createdateStart;
	}

	public Date getCreatedateEnd() {
		return createdateEnd;
	}

	public void setCreatedateEnd(Date createdateEnd) {
		this.createdateEnd = createdateEnd;
	}

	public String getfPrefix() {
		return fPrefix;
	}

	public void setfPrefix(String fPrefix) {
		this.fPrefix = fPrefix;
	}

	public String getfStatus() {
		return fStatus;
	}

	public void setfStatus(String fStatus) {
		this.fStatus = fStatus;
	}

	public Integer getfNum() {
		return fNum;
	}

	public void setfNum(Integer fNum) {
		this.fNum = fNum;
	}

	public String getfSalesman() {
		return fSalesman;
	}

	public void setfSalesman(String fSalesman) {
		this.fSalesman = fSalesman;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fTemplateId=").append(fTemplateId);
        sb.append(", fItemId=").append(fItemId);
        sb.append(", fUseMax=").append(fUseMax);
        sb.append(", fCreateMemory=").append(fCreateMemory);
        sb.append(", fInserttime=").append(fInserttime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}