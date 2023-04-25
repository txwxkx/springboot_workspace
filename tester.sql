
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
INSERT INTO todolist VALUES(todo_id_seq.nextval, 0, '잠자기');
commit;
SELECT * FROM todolist;

DROP TABLE todolist;
drop SEQUENCE todo_id_seq;

select * from board;
select * from members;

SELECT b.*, m.memberName
		FROM board b, members m
		WHERE b.memberEmail = m.memberEmail(+)
		AND num = 31;		
        
alter table board rename column email to memberemail;

commit;


