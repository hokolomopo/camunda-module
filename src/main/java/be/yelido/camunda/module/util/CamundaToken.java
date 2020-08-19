package be.yelido.camunda.module.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class CamundaToken implements Serializable {

    private String rootProcessBK;
    private String superProcessBK;
    private String currentProcessBK;
    private String processDefinitionKey;

    public CamundaToken(String currentProcessBK) {
        this.currentProcessBK = currentProcessBK;
    }

    /**
     * Create a new Camunda token from another token. The new token will have as parent process the given token
     *
     * @param token the token of the parent process
     * @param currentProcessBK the business key of the new token
     */
    public CamundaToken(CamundaToken token, String currentProcessBK) {
        this.currentProcessBK = currentProcessBK;
        this.rootProcessBK = token.rootProcessBK;
        this.superProcessBK = token.currentProcessBK;
    }

    /**
     *  Create a new token with a random business key
     *
     * @return the token
     */
    public static CamundaToken randomBKToken(){
        return new CamundaToken(UUID.randomUUID().toString());
    }

    /**
     * Create a new Camunda token from the parent token and a random current BK
     *
     * @param token the token to take the parent token BK
     */
    public static CamundaToken randomBKToken(CamundaToken token){
        return new CamundaToken(token, UUID.randomUUID().toString());
    }
}
