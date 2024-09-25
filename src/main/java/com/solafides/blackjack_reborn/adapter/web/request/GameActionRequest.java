package com.solafides.blackjack_reborn.adapter.web.request;

import com.solafides.blackjack_reborn.domain.BlackjackAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameActionRequest {
    private BlackjackAction action;
}
