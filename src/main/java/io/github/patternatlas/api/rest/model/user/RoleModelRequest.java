package io.github.patternatlas.api.rest.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleModelRequest {

    private boolean checkboxValue;
}
