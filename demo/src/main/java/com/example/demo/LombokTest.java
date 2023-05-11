package com.example.demo;

//웹이 아닌 콘솔 창에서만 출력 결과를 확인할 것이기 때문에 Run As > Java Application

public class LombokTest {

	public static void main(String[] args) {
		MemDTO dto = new MemDTO();
		dto.setName("홍길동");
		dto.setAge(30);
		dto.setLoc("서울");
		
		System.out.printf("%s %d %s\n", dto.getName(), dto.getAge(), dto.getLoc());

		MemDTO dto2 = new MemDTO("고수", 25, "경기");
		System.out.printf("%s %d %s\n", dto2.getName(), dto2.getAge(), dto2.getLoc());
		
	}

}//end class
