    package com.jarproject.controller;

    import com.jarproject.dto.CourseDto;
    import com.jarproject.entity.Course;
    import com.jarproject.service.CloudinaryService;
    import com.jarproject.service.Course.CourseService;
    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import javax.validation.Valid;
    import java.io.IOException;
    import java.util.List;
    import java.util.Optional;

    @Controller
    @RequestMapping("admin/course")
    public class AdminCourseController{
        @Autowired
        private CourseService courseService;
        @Autowired
        private ModelMapper modelMapper;
        @Autowired
        private CloudinaryService cloudinaryService;

        @GetMapping("list")
        public String list(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String orderBy,
                           @RequestParam(defaultValue = "asc") String orderType,
                           @RequestParam(defaultValue = "true") boolean status,
                           @RequestParam(required = false) String kw) {
            getData(model, page, orderBy, orderType, status, kw);
            model.addAttribute("isShowForm", false);
            model.addAttribute("isEdit", false);
            return "admin/course-management/course-list";
        }

        private void getData(Model model, int page, String orderBy, String orderType, boolean status, String kw){
            int size = 5;

            if (orderBy != null && !orderBy.equals("id") && !orderBy.equals("name")) {
                orderBy = null;
            }

            if (!orderType.equalsIgnoreCase("asc") && !orderType.equalsIgnoreCase("desc")) {
                orderType = "asc";
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
            model.addAttribute("orderBy", orderBy);
            model.addAttribute("orderType", orderType);
            model.addAttribute("kw", kw);
            model.addAttribute("status", status);

        }

        @GetMapping("showForm")
        public String showFormAdd(@RequestParam(required = false) String id, Model model){
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
            getData(model, 1, null, "asc", true, null);
            return "admin/course-management/course-list";
        }

        @PostMapping("save")
        public String save(@Valid @ModelAttribute("course") CourseDto courseDto,
                           BindingResult bindingResult,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           Model model) throws IOException {

            if (bindingResult.hasErrors()) {
                model.addAttribute("course", courseDto);
                model.addAttribute("isShowForm", true);
                model.addAttribute("isEdit", courseDto.getId() != null && !courseDto.getId().isEmpty());
                getData(model, 1, null, "asc", true, null);
                return "admin/course-management/course-list";
            }

            Course existingCourse = courseService.findByName(courseDto.getName());
            if (existingCourse != null && !existingCourse.getId().equals(courseDto.getId())) {
                model.addAttribute("error", "Tên khóa học đã tồn tại");
                model.addAttribute("course", courseDto);
                model.addAttribute("isShowForm", true);
                model.addAttribute("isEdit", courseDto.getId() != null && !courseDto.getId().isEmpty());
                getData(model, 1, null, "asc", true, null);
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
                    model.addAttribute("isEdit", false);
                    getData(model, 1, null, "asc", true, null);
                    return "admin/course-management/course-list";
                }
            }

            Course course = modelMapper.map(courseDto, Course.class);
            if (course.getId() == null || course.getId().isEmpty()) {
                course.setStatus(true);
                courseService.save(course);
                model.addAttribute("success", "Thêm khóa học thành công!");
            } else {
                courseService.update(course);
                model.addAttribute("success", "Cập nhật khóa học thành công!");
            }

            return "redirect:/admin/course/list";
        }





        @GetMapping("delete/{id}")
        public String delete(@PathVariable String id, Model model) {
            courseService.delete(id);
            return "redirect:/admin/course/list";
        }
    }
