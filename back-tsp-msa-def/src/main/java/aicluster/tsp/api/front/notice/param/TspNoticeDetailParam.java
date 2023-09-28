package aicluster.tsp.api.front.notice.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TspNoticeDetailParam implements Serializable {
    private static final long serialVersionUID = -4497484126418165266L;

    @ApiModelProperty(value = "게시글 ID")
    private String articleId;
    @ApiModelProperty(value = "게시판 ID")
    private String boardId;
    @ApiModelProperty(value = "제목")
    private String title;
    @ApiModelProperty(value = "게시글")
    private String article;
    @ApiModelProperty(value = "첨부파일그룹ID")
    private String attachmentGroupId;
    @ApiModelProperty(value = "이미지그룹ID")
    private String imageGroupId;
    //@ApiModelProperty(value = "")
}
