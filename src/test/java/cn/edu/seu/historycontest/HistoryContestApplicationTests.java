package cn.edu.seu.historycontest;

import cn.edu.seu.historycontest.entity.Department;
import cn.edu.seu.historycontest.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HistoryContestApplicationTests {

    @Autowired
    private DepartmentService departmentService;

   // @Test
    void insertDepartment() {
        departmentService.save(new Department("G13", "材料科学与工程学院"));
        departmentService.save(new Department("G22", "电气工程学院"));
        departmentService.save(new Department("D12", "电子科学与工程学院"));
        departmentService.save(new Department("DE3", "法学院"));
        departmentService.save(new Department("42", "公共卫生学院"));
        departmentService.save(new Department("19Q,G32", "化学化工学院"));
        departmentService.save(new Department("G11,GS2", "机械工程学院"));
        departmentService.save(new Department("JS1,JS2", "计算机科学与工程学院"));
        departmentService.save(new Department("01A", "建筑学院"));
        departmentService.save(new Department("TJ2", "交通学院"));
        departmentService.save(new Department("14C,DE2,GS1", "经济管理学院"));
        departmentService.save(new Department("436,43A,G33", "临床医学院"));
        departmentService.save(new Department("G12,G31", "能源与环境学院"));
        departmentService.save(new Department("13A,13Q", "人文学院"));
        departmentService.save(new Department("G34", "生命科学与技术学院"));
        departmentService.save(new Department("112,D13", "生物科学与医学工程学院"));
        departmentService.save(new Department("07Q,LK1", "数学学院"));
        departmentService.save(new Department("TJ1", "土木工程学院"));
        departmentService.save(new Department("171,172", "外国语学院"));
        departmentService.save(new Department("JS3", "网络空间安全学院"));
        departmentService.save(new Department("615,616", "吴健雄学院"));
        departmentService.save(new Department("10Q", "物理学院"));
        departmentService.save(new Department("D11,D21", "信息科学与工程学院"));
        departmentService.save(new Department("900", "学生处"));
        departmentService.save(new Department("G23", "仪器科学与工程学院"));
        departmentService.save(new Department("24A", "艺术学院"));
        departmentService.save(new Department("G21", "自动化学院"));
    }


}
