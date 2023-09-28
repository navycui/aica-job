package aicluster.tsp.api.front.notice.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TspNoticeListParam implements Serializable {

    private static final long serialVersionUID = -4826436735230529236L;

    @ApiModelProperty(value = "게시글 ID")
    private String articleId;
    @ApiModelProperty(value = "게시판 ID")
    private String boardId;
    @ApiModelProperty(value = "제목")
    private String title;
    @ApiModelProperty(value = "게시글")
    private String article;
    @ApiModelProperty(value = "공지여부(중요공지)")
    private String notice;
    @ApiModelProperty(value = "조회수")
    private Integer readCnt;
    @ApiModelProperty(value = "생성일시")
    private Date createdDt;

    public Date getCreatedDt() {
        if (this.createdDt != null) {
            return new Date(this.createdDt.getTime());
        }
        return null;
    }
    public void setCreatedDt(Date createdDt) {
        this.createdDt = null;
        if (createdDt != null) {
            this.createdDt = new Date(createdDt.getTime());
        }
    }
}