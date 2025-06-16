    package com.jarproject.controller;

    import com.jarproject.dto.CourseDto;
    import com.jarproject.entity.Course;
    import com.jarproject.entity.Student;
    import com.jarproject.service.CloudinaryService;
    import com.jarproject.service.course.CourseService;
    import com.jarproject.service.student.StudentService;
    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import javax.servlet.http.HttpSession;
    import javax.validation.Valid;
    import java.io.IOException;
    import java.util.List;

    @Controller
    @RequestMapping("admin/course")
    public class AdminCourseController{
        @Autowired
        private CourseService courseService;
        @Autowired
        private ModelMapper modelMapper;
        @Autowired
        private CloudinaryService cloudinaryService;
        @Autowired
        private HttpSession session;
        @Autowired
        private StudentService studentService;

        @GetMapping
        public String adminCourse(){
            return "redirect:/admin/course/list";
        }

        @GetMapping("list")
        public String list(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "id_asc") String sort,
                           @RequestParam(defaultValue = "true") boolean status,
                           @RequestParam(required = false) String kw) {

            Integer curUser = (Integer) session.getAttribute("curUser");
            if(curUser == null){
                return "redirect:/login";
            }

            Student sessionStudent = studentService.findById(curUser);
            if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
                return "redirect:/login";
            }

            getData(model, page, sort, status, kw);
            model.addAttribute("isShowForm", false);
            model.addAttribute("isEdit", false);
            model.addAttribute("isDelete", false);

            return "admin/course-management/course-list";
        }

        private void getData(Model model, int page, String sort, boolean status, String kw) {
            int size = 5;

            String orderBy = "id";
            String orderType = "asc";

            if (sort != null && !sort.trim().isEmpty()) {
                String[] sortParts = sort.split("_");
                if (sortParts.length == 2) {
                    String field = sortParts[0];
                    String direction = sortParts[1];

                    // Validate field
                    if ("id".equals(field) || "name".equals(field)) {
                        orderBy = field;
                    }

                    // Validate direction
                    if ("asc".equals(direction) || "desc".equals(direction)) {
                        orderType = direction;
                    }
                }
            }

            List<Course> courses;
            long totalItems;
            long totalPages;

            if (kw != null && !kw.trim().isEmpty()) {
                courses = courseService.searchByName(kw, page, size, orderBy, orderType, status);
                totalItems = courseService.countByName(kw, status);
            } else {
                courses = courseService.findAll(page, size, orderBy, orderType, status);
                totalItems = courseService.count(status);
            }

            totalPages = (long) Math.ceil((double) totalItems / size);

            model.addAttribute("courses", courses);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("sort", sort);
            model.addAttribute("kw", kw);
            model.addAttribute("status", status);
        }



        @GetMapping("showForm")
        public String showFormAdd(@RequestParam(required = false) String id, Model model){
            Integer curUser = (Integer) session.getAttribute("curUser");
            if(curUser == null){
                return "redirect:/login";
            }
            Student sessionStudent = studentService.findById(curUser);
            if(sessionStudent == null || !sessionStudent.isStatus()){
                return "redirect:/login";
            }
            CourseDto courseDto;


            if(id != null && !id.trim().isEmpty()) {
                Course course = courseService.findById(id);
                if(course != null) {
                    courseDto = modelMapper.map(course, CourseDto.class);
                    model.addAttribute("isEdit", true);
                } else {
                    courseDto = new CourseDto();
                    model.addAttribute("isEdit", false);
                }
            } else {
                courseDto = new CourseDto();
                model.addAttribute("isEdit", false);
            }

            model.addAttribute("course", courseDto);
            model.addAttribute("isShowForm", true);
            getData(model, 1, "id_asc", true, null);
            return "admin/course-management/course-list";
        }

        @PostMapping("save")
        public String save(@Valid @ModelAttribute("course") CourseDto courseDto,
                           BindingResult bindingResult,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           Model model) throws IOException {
            Integer curUser = (Integer) session.getAttribute("curUser");
            if(curUser == null){
                return "redirect:/login";
            }
            Student sessionStudent = studentService.findById(curUser);
            if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
                return "redirect:/login";
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("course", courseDto);
                model.addAttribute("isShowForm", true);
                model.addAttribute("isEdit", false);
                getData(model, 1, "id_asc", true, null);
                return "admin/course-management/course-list";
            }

            Course existingCourse = courseService.findByName(courseDto.getName());
            if (existingCourse != null) {
                model.addAttribute("error", "Tên khóa học đã tồn tại");
                model.addAttribute("course", courseDto);
                model.addAttribute("isShowForm", true);
                model.addAttribute("isEdit", false);
                getData(model, 1, "id_asc", true, null);
                return "admin/course-management/course-list";
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String fileName = cloudinaryService.uploadFile(imageFile);
                    courseDto.setImage(fileName);
                } catch (Exception e) {
                    model.addAttribute("imageError", "Lỗi khi upload ảnh");
                    model.addAttribute("course", courseDto);
                    model.addAttribute("isShowForm", true);
                    model.addAttribute("isEdit", false);
                    getData(model, 1, "id_asc", true, null);
                    return "admin/course-management/course-list";
                }
            }else{
                model.addAttribute("imageError", "Ảnh không được để trống");
                model.addAttribute("course", courseDto);
                model.addAttribute("isShowForm", true);
                model.addAttribute("isEdit", false);
                getData(model, 1, "id_asc", true, null);
                return "admin/course-management/course-list";
            }

            Course course = modelMapper.map(courseDto, Course.class);
            course.setStatus(true);
            courseService.save(course);
            model.addAttribute("success", "Thêm khóa học thành công!");

            return "redirect:/admin/course/list";
        }

        @PostMapping("update")
        public String update(@Valid @ModelAttribute("course") CourseDto courseDto,
                             BindingResult bindingResult,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Model model) throws IOException {
            Integer curUser = (Integer) session.getAttribute("curUser");
            if(curUser == null){
                return "redirect:/login";
            }
            Student sessionStudent = studentService.findById(curUser);
            if(sessionStudent == null || !sessionStudent.isStatus()){
                return "redirect:/login";
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("course", courseDto);
                model.addAttribute("isShowForm", true);
                model.addAttribute("isEdit", true);
                getData(model, 1,  "id_asc", true, null);
                return "admin/course-management/course-list";
            }

            Course existingCourse = courseService.findByName(courseDto.getName());
            if (existingCourse != null && !existingCourse.getId().equals(courseDto.getId())) {
                model.addAttribute("error", "Tên khóa học đã tồn tại");
                model.addAttribute("course", courseDto);
                model.addAttribute("isShowForm", true);
                model.addAttribute("isEdit", true);
                getData(model, 1,  "id_asc", true, null);
                return "admin/course-management/course-list";
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String fileName = cloudinaryService.uploadFile(imageFile);
                    courseDto.setImage(fileName);
                } catch (Exception e) {
                    model.addAttribute("imageError", "Lỗi khi upload ảnh: " + e.getMessage());
                    model.addAttribute("course", courseDto);
                    model.addAttribute("isShowForm", true);
                    model.addAttribute("isEdit", true);
                    getData(model, 1,  "id_asc", true, null);
                    return "admin/course-management/course-list";
                }
            }

            Course course = modelMapper.map(courseDto, Course.class);
            courseService.update(course);
            model.addAttribute("success", "Cập nhật khóa học thành công!");

            return "redirect:/admin/course/list";
        }

        @GetMapping("delete/{id}")
        public String showDeleteForm(@PathVariable("id") String id, Model model) {
            Integer curUser = (Integer) session.getAttribute("curUser");
            if(curUser == null){
                return "redirect:/login";
            }
            Student sessionStudent = studentService.findById(curUser);
            if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
                return "redirect:/login";
            }

            if (id == null || id.trim().isEmpty()) {
                return "redirect:/admin/course/list";
            }
            Course course = courseService.findById(id);
            if(course == null){
                return "redirect:/admin/course/list";
            }

            CourseDto courseDto = modelMapper.map(course, CourseDto.class);

            if(course.isStatus()){
                model.addAttribute("message", "Bạn có chắc muốn xoá " + courseDto.getName() + " ra khỏi khoá học");
                model.addAttribute("button", "Xoá");
            }else{
                model.addAttribute("message", "Bạn có chắc chắn muốn khôi phục khóa học " + courseDto.getName() + " không?");
                model.addAttribute("button", "Khôi phục");
            }
            model.addAttribute("course", courseDto);
            model.addAttribute("status", course.isStatus());
            model.addAttribute("isDelete", true);
            getData(model, 1,  "id_asc", true, null);
            return "admin/course-management/course-list";
        }

        @PostMapping("delete/{id}")
        public String delete(@PathVariable String id, Model model) {
            Integer curUser = (Integer) session.getAttribute("curUser");
            if(curUser == null){
                return "redirect:/login";
            }
            Student sessionStudent = studentService.findById(curUser);
            if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
                return "redirect:/login";
            }

            courseService.delete(id);
            return "redirect:/admin/course/list";
        }
    }
