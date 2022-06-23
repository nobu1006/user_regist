package com.example.user_regist.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.user_regist.common.AppException;
import com.example.user_regist.domain.RegistRequest;

@Repository
public class RegistRequestRepository {

    private RowMapper<RegistRequest> REGIST_REQUEST_ROW_MAPPER = new BeanPropertyRowMapper<>(RegistRequest.class);

    @Autowired
    NamedParameterJdbcTemplate template;
    
    public RegistRequest findByEmail(String email) {
        String sql = """
            select
                id
                , email
                , unique_key
                , regist_date
                , del_flg
            from regist_requests
            where
                email = :email
            and
                del_flg = false
            order by regist_date desc
            limit 1;
        """;

        SqlParameterSource params = new MapSqlParameterSource().addValue("email", email);
        try {
            return template.queryForObject(sql, params, REGIST_REQUEST_ROW_MAPPER);
        } catch (Exception e) {
            return null;
        }
    }

    public RegistRequest findByUniquKey(String uniquKey) {
        String sql = """
            select
                id
                , email
                , unique_key
                , regist_date
                , del_flg
            from regist_requests
            where
                unique_key = :unique_key
            and
                del_flg = false
            order by regist_date desc
            limit 1;
        """;

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("unique_key", uniquKey);
        try {
            return template.queryForObject(sql, params, REGIST_REQUEST_ROW_MAPPER);
        } catch (Exception e) {
            return null;
        }
    }

    public void insert(RegistRequest registRequest) {

        String sql = """
            insert into regist_requests (
                email
                , unique_key
                , regist_date
                , del_flg
            ) values (
                :email
                , :unique_key
                , now()
                , false
            );
        """;

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("email", registRequest.getEmail())
            .addValue("unique_key", registRequest.getUniqueKey());

        int count = template.update(sql, params);
        if (count != 1) {
            throw new AppException();
        }
    }

    public int deleteByEmail(String email) {
        String sql = """
            update regist_requests set
                del_flg = true
            where
                email = :email;
        """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);

        return template.update(sql, params);
    }
    
}
