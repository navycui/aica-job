package aicluster.member.api.auth.dto;

import aicluster.member.common.entity.CmmtMberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String memberId;
    private String loginId;
    private String authorityId;

    public static MemberResponseDto of(CmmtMberInfo member) {
        return new MemberResponseDto(member.getMemberId(), member.getLoginId(), member.getAuthorityId());
    }
}
