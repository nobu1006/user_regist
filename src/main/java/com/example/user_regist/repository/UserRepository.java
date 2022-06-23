package com.example.user_regist.repository;

import com.example.user_regist.common.AppException;
import com.example.user_regist.domain.RegistRequest;
import com.example.user_regist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private RowMapper<User> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

    @Autowired
    NamedParameterJdbcTemplate template;

    public void insert(User user) {
        String sql = """
            insert into users (
                name
                , name_kana
                , email
                , password
                , zip_code
                , address
                , telephone
                , regist_date
                , update_date
                , del_flg
            ) values (
                :name
                , :nameKana
                , :email
                , :password
                , :zipCode
                , :address
                , :telephone
                , now()
                , now()
                , false
            );
        """;

        SqlParameterSource params = new BeanPropertySqlParameterSource(user);

        int count = template.update(sql, params);
        if (count != 1) {
            throw new AppException();
        }
    }

}
