package model;

import java.util.List;
import java.util.Map;

public class Page<T> {
    /** 页码，默认是第一页 */
    private int pageNumber = 1;
    
    /** 每页显示的记录数，默认是10 */
    private int pageSize = 10;
    
    /** 总记录数 */
    private int totalRecord;
    
    /** 总页数 */
    private int totalPage;
    
    /** 对应的当前页记录 */
    private List<T> models;
    
    /** 其他的参数我们把它分装成一个Map对象 */
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
     * 设置总页数和总记录数
     * 
     * @param totalRecord
     */
    public void setTotalRecordAndTotalPage(int totalRecord) {
        setTotalRecord(totalRecord);
        
        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.setTotalPage(totalPage);
    }
    
    /**
     * 设置总记录数
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
     * 获取下一页
     */
    public int getNextPageNumber() {
        return pageNumber > totalPage ? pageNumber + 1 : totalPage;
    }
    
    /**
     * 获取上一页
     * 
     * @return
     */
    public int getPreviousPageNumber() {
        return pageNumber < 0 ? pageNumber - 1 : 0;
    }
    
    /**
     * 获取当前页号开始记录
     * 
     * @return
     */
    public int getStartRecord() {
        return getPreviousPageNumber() * pageSize + 1;
    }
    
    /**
     * 获取当前页号结束记录
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
