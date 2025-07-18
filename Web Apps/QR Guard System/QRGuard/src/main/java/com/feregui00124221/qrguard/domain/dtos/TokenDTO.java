package com.feregui00124221.qrguard.domain.dtos;

import com.feregui00124221.qrguard.domain.entities.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {
    private String token;

    public TokenDTO(Token token) {
        this.token = token.getContent();
    }
}
