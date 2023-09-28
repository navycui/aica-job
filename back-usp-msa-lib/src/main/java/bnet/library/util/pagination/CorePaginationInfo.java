package bnet.library.util.pagination;

public class CorePaginationInfo {
	private Long page;
	private Long itemsPerPage;
	private Long totalItems;
	private Long beginRowNum;
	private Long endRowNum;

	public CorePaginationInfo(Long page, Long itemsPerPage, Long totalItems) {
		if (itemsPerPage == null || itemsPerPage < 1) {
			itemsPerPage = 1L;
		}
		if (totalItems == null || totalItems < 1) {
			totalItems = 0L;
		}
    	long lastPage = (int)(totalItems / itemsPerPage);
        if (totalItems % itemsPerPage > 0) {
            lastPage++;
        }
        if (page == null || page < 1) {
            page = 1L;
        }
        if (page > lastPage) {
            page = lastPage;
        }

        this.page = page;
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
        this.beginRowNum = itemsPerPage * (page-1) + 1;
        this.beginRowNum = beginRowNum < 1 ? 1 : beginRowNum;
        this.endRowNum = itemsPerPage * page;
	}

	public Long getPage() {
		return page;
	}

	public Long getItemsPerPage() {
		return itemsPerPage;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public Long getBeginRowNum() {
		return beginRowNum;
	}

	public Long getEndRowNum() {
		return endRowNum;
	}

}
