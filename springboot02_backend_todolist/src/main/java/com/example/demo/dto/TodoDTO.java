package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
CREATE TABLE todolist(
id number  PRIMARY KEY,
completed number(1) DEFAULT 0,
todoname  VARCHAR2(100) NOT NULL);


CREATE SEQUENCE todo_id_seq
START WITH 1
INCREMENT By 1
NOCACHE
NOCYCLE;


INSERT INTO todolist VALUES(todo_id_seq.nextval, 0, '여행');
commit;
SELECT * FROM todolist;
*/

/*
@Data : @NoArgsConstructor, @AllArgsConstructor, @Getter, @Setter, 
		@ToString, @EqualAndHashCode를 합쳐놓은 어노테이션이다.
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoDTO {
	private int id;
	private int completed;
	private String todoname;
}
