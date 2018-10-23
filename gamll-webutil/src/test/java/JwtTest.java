
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    private String key="jjdjjfejwoifawjfojgohagaijjsoifosjfoiujrhjgaj";
    @Test
    public void createJwtTest(){
        Map<String,Object> user = new HashMap<>();
        user.put("id","123");
        user.put("userName","zhangsan");
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        System.out.println("key:"+secretKey);
        String s = Jwts.builder().addClaims(user).signWith(secretKey, SignatureAlgorithm.HS256).compact();
        System.out.println("skey:"+s);
    }

    @Test
    public void verify(){
String s="eyJhbGciOiJIUzI1Ni9.eyJpZCI6IjEyMyIsInVzZXJOYW1lIjoiemhhbmdzYW4ifQ.EjDiieAcZ68BL9RRQkXWa427qKIr0mQ1piY2I4EWCv4\n";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
       Jwts.parser().setSigningKey(secretKey).parse(s);


    }
}
