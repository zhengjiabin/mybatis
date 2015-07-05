package model;

import java.util.List;
import java.util.Map;

public class Page<T> {
    /** ҳ�룬Ĭ���ǵ�һҳ */
    private int pageNumber = 1;
    
    /** ÿҳ��ʾ�ļ�¼����Ĭ����10 */
    private int pageSize = 10;
    
    /** �ܼ�¼�� */
    private int totalRecord;
    
    /** ��ҳ�� */
    private int totalPage;
    
    /** ��Ӧ�ĵ�ǰҳ��¼ */
    private List<T> models;
    
    /** �����Ĳ������ǰ�����װ��һ��Map���� */
    private Map<String, Object> params;
    
    public Page() {
        super();
    }
    
    public Page(int pageNumber, int pageSize) {
        super();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
    
    public Page(int pageNumber, int pageSize, List<T> models, Map<String, Object> params) {
        super();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.models = models;
        this.params = params;
    }
    
    public int getPageNumber() {
        return pageNumber;
    }
    
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getTotalRecord() {
        return totalRecord;
    }
    
    /**
     * ������ҳ�����ܼ�¼��
     * 
     * @param totalRecord
     */
    public void setTotalRecordAndTotalPage(int totalRecord) {
        setTotalRecord(totalRecord);
        
        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.setTotalPage(totalPage);
    }
    
    /**
     * �����ܼ�¼��
     * 
     * @param totalRecord
     */
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
    
    public int getTotalPage() {
        return totalPage;
    }
    
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    
    public List<T> getModels() {
        return models;
    }
    
    public void setModels(List<T> models) {
        this.models = models;
    }
    
    public Map<String, Object> getParams() {
        return params;
    }
    
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    /**
     * ��ȡ��һҳ
     */
    public int getNextPageNumber() {
        return pageNumber > totalPage ? pageNumber + 1 : totalPage;
    }
    
    /**
     * ��ȡ��һҳ
     * 
     * @return
     */
    public int getPreviousPageNumber() {
        return pageNumber < 0 ? pageNumber - 1 : 0;
    }
    
    /**
     * ��ȡ��ǰҳ�ſ�ʼ��¼
     * 
     * @return
     */
    public int getStartRecord() {
        return getPreviousPageNumber() * pageSize + 1;
    }
    
    /**
     * ��ȡ��ǰҳ�Ž�����¼
     * 
     * @return
     */
    public int getEndRecord() {
        return pageNumber * pageSize;
    }
    
    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder();
        builder.append("Page [pageNo=");
        builder.append(pageNumber);
        builder.append(", pageSize=");
        builder.append(pageSize);
        builder.append(", results=");
        builder.append(models);
        builder.append(", totalPage=");
        builder.append(totalPage);
        builder.append(", totalRecord=");
        builder.append(totalRecord);
        builder.append("]");
        
        return builder.toString();
    }
}
