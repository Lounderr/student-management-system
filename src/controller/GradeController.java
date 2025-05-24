package controller;

import data.model.Grade;
import data.model.repository.impl.GradeRepositoryOld;

import java.sql.SQLException;
import java.util.List;

public class GradeController {
    private final GradeRepositoryOld gradeRepository;

    public GradeController(GradeRepositoryOld gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> findGradesByStudentId(int studentId) throws SQLException {
        return this.gradeRepository.findByStudentId(studentId);
    }
}
