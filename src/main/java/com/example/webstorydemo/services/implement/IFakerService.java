package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.entity.*;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.repository.*;
import com.example.webstorydemo.services.FakerService;
import com.example.webstorydemo.utils.SlugUtils;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class IFakerService implements FakerService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StoryRepository storyRepository;
    private final StoryChapterRepository chapterRepository;
    private final CommentRepository commentRepository;
    private final HistoryRepository historyRepository;
    private final UserFollowRepository followRepository;

    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker(Locale.ENGLISH);

    private final Faker fakerVi = new Faker(new Locale("vi"));
    private final Random random = new Random();

    @Override
    @Transactional
    public void generateCustomUsers(Integer userCount) {
        try{
            Roles customer = roleRepository.findRolesByRoleName(Roles.BaseRole.USER).orElseThrow(() ->
                    new RuntimeException("Role CUSTOMER not found"));
            for (int i = 0; i < userCount; i++) {
                String avatarUrl = "https://randomuser.me/api/portraits/men/" + (i+1) + ".jpg";
                Users user = new Users();
                user.setUsername(faker.name().username());
                user.setAvatar(avatarUrl);
                user.setDob(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                user.setEmail(faker.internet().emailAddress());
                user.setHashPassword(passwordEncoder.encode("123456"));
                user.setStatus(Users.Status.NORMAL);
                user.setVerifyCode(null);
                user.setRolesList(Collections.singletonList(customer));
                userRepository.save(user);
            }
            log.info("Generated 20 fake customer users");
        } catch (Exception e){
            e.printStackTrace();
            log.error("Fake root error!");
        }
    }

    @Override
    @Transactional
    public void generateAdminUsers() {
        try{
            Roles customer = roleRepository.findRolesByRoleName(Roles.BaseRole.USER).orElseThrow(() ->
                    new RuntimeException("Role CUSTOMER not found"));
            Roles admin = roleRepository.findRolesByRoleName(Roles.BaseRole.ADMIN).orElseThrow(() ->
                    new RuntimeException("Role ADMIN not found"));
            Users user = new Users();
            user.setUsername("anhoang");
            user.setAvatar("https://randomuser.me/api/portraits/men/50.jpg");
            user.setDob(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            user.setEmail("anhoang@gmail.com");
            user.setHashPassword(passwordEncoder.encode("123456"));
            user.setStatus(Users.Status.NORMAL);
            user.setVerifyCode(null);
            user.setRolesList(Arrays.asList(customer, admin));
            userRepository.save(user);

        } catch (Exception e){
            e.printStackTrace();
            log.error("Fake admin error!");
        }
    }

    @Override
    @Transactional
    public void generateCategory() {
        List<String> genres = Arrays.asList(
                "Tiên hiệp", "Huyền huyễn", "Đô thị", "Ngôn tình", "Kiếm hiệp", "Hài hước",
                "Khoa học viễn tưởng", "Xuyên không", "Trọng sinh", "Hệ thống", "Dị giới", "Học đường",
                "Lịch sử", "Quân sự", "Cổ đại", "Hiện đại", "Trinh thám", "Kinh dị", "Hành động",
                "Phiêu lưu", "Ma pháp", "Ma quái", "Đam mỹ", "Bách hợp", "Game", "Võng du", "Thám hiểm",
                "Tu chân", "Light novel", "Truyện teen", "Slice of life", "Drama", "Psychological",
                "Romance", "Fantasy"
        );
        try {
            List<Category> categoryList = new ArrayList<>();
            int idx = 1;
            for (String cateName : genres) {
                Category category = new Category();
                category.setName(cateName);
                category.setSlug(SlugUtils.titleToSlug(cateName));
                categoryList.add(category);
                idx++;
            }
            categoryRepository.saveAll(categoryList);
            log.info("Generate {} category success!", idx - 1);
        } catch (Exception e){
            e.printStackTrace();
            log.error("Fake category error!");
        }
    }

    @Override
    @Transactional
    public void generateStory(int storyCount) {
        List<String> image = List.of(
                "https://truyen.audio/_next/image?url=https%3A%2F%2Fstatic.8cache.com%2Fcover%2FeJzLyTDWzwoqqIr0z3TzzEnxcclOLPEIrgjz903KMzcpripxj4py8TYz8LNwSstwT3c2yk42qLLIL8vy8s8s8U1zLPUwqTDL1E3z9ne2CPco1jWu8otwCvK0LTcyNNXNMDYyAgBGrx9n%2Fdai-phung-da-canh-nhan.jpg&w=384&q=75s",
                "https://bookio.edu.vn/wp-content/uploads/2024/06/chang-re-manh-nhat-lich-su.gif",
                "https://pic7.iqiyipic.com/image/20230413/60/f7/a_100432281_m_601_vi_260_360.jpg",
                "https://m.media-amazon.com/images/M/MV5BZTNjOWI0ZTAtOGY1OS00ZGU0LWEyOWYtMjhkYjdlYmVjMDk2XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                "https://upload.wikimedia.org/wikipedia/vi/thumb/5/51/Roku_de_Nashi_Majutsu_K%C5%8Dshi_to_Akashic_Records_light_novel_volume_1_cover.jpg/250px-Roku_de_Nashi_Majutsu_K%C5%8Dshi_to_Akashic_Records_light_novel_volume_1_cover.jpg",
                "https://m.media-amazon.com/images/M/MV5BNmI1MmYxNWQtY2E5NC00ZTlmLWIzZGEtNzM1YmE3NDA5NzhjXkEyXkFqcGc@._V1_.jpg",
                "https://product.hstatic.net/200000343865/product/doraemon-tieu-thuyet_nobita-va-cuoc-phieu-luu-vao-the-gioi-trong-tranh_be6224391ef443948ceeeef4f933bec3_master.jpg",
                "https://product.hstatic.net/200000343865/product/tham_tu_lung_danh_conan_ban_nang_cap_bia_tap_1_ae0343adc05e43b9840ab4d9c7eaa50b_master.jpg",
                "https://m.media-amazon.com/images/M/MV5BYjI1NWY4NjgtNzE2MC00MmZhLTliMjQtODllNDc0YjgyYzAwXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                "https://m.media-amazon.com/images/M/MV5BMTk1MGM5ZDQtMWFkZS00YTUyLWIzYWYtZTQwYWYzNzQ3MTMyXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                "https://m.media-amazon.com/images/M/MV5BN2NhYzU2NDEtYzI1NS00MjgzLThjZGUtOTYxNGJkZjZmNDdjXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-W_VcxMsJiPbTgYkjXHfwJ4LCugmB7OeFbQ&s",
                "https://images2.thanhnien.vn/528068263637045248/2025/3/6/thanh-guom-diet-quy-17412472874981233436600.jpg",
                "https://upload.wikimedia.org/wikipedia/vi/thumb/6/65/The_Rising_of_the_Shield_Hero_light_novel_vol_1.jpg/250px-The_Rising_of_the_Shield_Hero_light_novel_vol_1.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTp-aHR-SlFEkjXQglZ0NZC3QfNslKXA6myRg&s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIfUUQtP3gx9LXiUYPGLe7ZGl_IVXJXcSEZw&s",
                "https://i5.walmartimages.com/seo/Yu-GI-Oh-3-In-1-Edition-Yu-Gi-Oh-3-In-1-Edition-Vol-1-Includes-Vols-1-2-amp-3-Book-1-Paperback-9781421579245_d75cb070-725f-48ce-a13b-a83c3ff4b7d4_1.6eef537ad434d05fb1931b2eca92624d.jpeg",
                "https://image.tmdb.org/t/p/original/txaCsPyVn627Nc9AZXUSQDn3nRw.jpg",
                "https://bookio.edu.vn/wp-content/uploads/2024/06/kiem-lai-phong-hoa-hi-chu-hau.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTihxIwq2PetM_K4e4yeRG2dX8mv57jC1VrLQ&s",
                "https://upload.wikimedia.org/wikipedia/vi/7/7e/Zhu_Xian_Poster.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTJWhnV9eh4behmFhMYeXmQSKbvxP-9bQnztKK_WPxm59VC9I4_d_rJPc751Od33DVQcfw&usqp=CAU",
                "https://i.pinimg.com/736x/d2/26/93/d22693a6ca0ecf551e3b99ec435f5c75.jpg"
        );
        try {
            List<Story> storyList = new ArrayList<>();
            for (int i = 1; i <= storyCount; i++) {
                Story story = new Story();
                story.setTitle(fakerVi.book().title());
                story.setSlug(SlugUtils.titleToSlug(story.getTitle()) + "-" + i);
                story.setAuthor(fakerVi.name().fullName());
                story.setDescription(fakerVi.lorem().sentence(30) + "...");
                story.setCoverUrl(image.get(random.nextInt(0, image.size()-1)));
                story.setStatus(faker.options().option(Story.Status.class));
                story.setFollowCount(0L);
                story.setReviewCount(0L);
                story.setViewCount(random.nextLong(1000, 10000000));

                List<Category> categoryList = categoryRepository
                        .findRandomCategories(random.nextInt(4, 7));
                story.setCategoryList(categoryList);

                List<StoryChapter> chapterList = new ArrayList<>();
                int chapterCount = random.nextInt(5, 30);
                for (int j = 1; j <= chapterCount; j++) {
                    StoryChapter chapter = new StoryChapter();
                    chapter.setChapterNumber(j);
                    chapter.setTitle(fakerVi.book().title());
                    chapter.setSlug("chapter-" + j);
                    chapter.setStory(story);
                    StringBuilder content = new StringBuilder();
                    for(int k = 0; k<10; k++){
                        content.append("\n")
                                .append(fakerVi.lorem().sentence(random.nextInt(10, 50)));
                    }
                    chapter.setChapterContent(
                            new ChapterContent(content.toString(), chapter));
                    chapterList.add(chapter);
                }
                story.setChapterList(chapterList);
                story.setChapterUpdate(chapterCount);
                story.setChapterUpdateAt(LocalDateTime.now());
                story.setEnable(true);
                storyList.add(story);
            }
            storyRepository.saveAll(storyList);
            log.info("Generate {} story success!", storyCount);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fake story error!");
        }
    }

    @Override
    @Transactional
    public void generateComment(){
        List<StoryChapter> chapterList = chapterRepository.findAll();
        List<Users> usersList = userRepository.findAll();
        for(StoryChapter chapter : chapterList){
            List<StoryComment> commentList = new ArrayList<>();
            Story story = chapter.getStory();
            for(Users users : usersList){
                StoryComment comment = new StoryComment();
                comment.setContent(fakerVi.lorem().sentence(20));
                comment.setLevel(0);
                comment.setStory(story);
                comment.setChapter(chapter);
                comment.setUsers(users);
                commentList.add(comment);
            }
            commentRepository.saveAll(commentList);
            story.setReviewCount(story.getReviewCount() + commentList.size());
            storyRepository.save(story);
        }
    }

    @Override
    @Transactional
    public void  generateHistory(){
        List<Users> usersList = userRepository.findAll();
        for (Users users : usersList){
            List<StoryChapter> randomList = chapterRepository.findRandom20Chapters();
            List<ReadingHistory> historyList = new ArrayList<>();
            for(StoryChapter chapter : randomList){
                ReadingHistory history = new ReadingHistory();
                history.setUsers(users);
                history.setChapter(chapter);
                history.setStory(chapter.getStory());
                historyList.add(history);
            }
            historyRepository.saveAll(historyList);
        }
    }

    @Override
    @Transactional
    public void fakeTopView() {
        try{
            List<Story> storyList = storyRepository.findAll();
            for(Story story : storyList){
                long viewTotal = 0;
                List<StoryView> viewList = new ArrayList<>();
                for(long i = 0; i<30; i++){
                    long viewCount = random.nextLong(1000, 100000);
                    viewTotal += viewCount;
                    LocalDate time =  LocalDate.now().minusDays(i);
                    StoryView view = new StoryView();
                    view.setStory(story);
                    view.setViewCount(viewCount);
                    view.setViewDate(time);
                    viewList.add(view);
                }
                story.setViewCount(viewTotal);
                story.setStoryViewList(viewList);
                storyRepository.save(story);
            }
            log.info("generate view of story success!");
        } catch (Exception e){
            log.error("faker view error!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public void fakeStoryFollow() {
        try{
            List<Users> usersList = userRepository.findAll();
            for (Users users : usersList){
                List<Story> storyList = storyRepository.findRandomStory(random.nextInt(20, 30));
                for(Story story : storyList){
                    UserFollow follow = new UserFollow();
                    follow.setStory(story);
                    follow.setUsers(users);
                    followRepository.save(follow);
                    story.setFollowCount(story.getFollowCount() + 1);
                    storyRepository.save(story);
                }
            }
        } catch (Exception e){
            log.error("faker view error!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }


}
