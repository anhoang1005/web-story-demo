package com.example.webstorydemo.model.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusReview {
    private Long normalCount;
    private Long verifyCount;
    private Long deleteCount;
    private Long blockCount;
}
