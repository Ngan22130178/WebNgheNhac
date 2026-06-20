package vn.edu.nlu.fit.musicweb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import vn.edu.nlu.fit.musicweb.model.*;
import vn.edu.nlu.fit.musicweb.repository.*;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner initDatabase(SongRepository songRepo, SongLyricsRepository lyricsRepo) {
        return args -> {
            songRepo.deleteAll();
            lyricsRepo.deleteAll();

            addSong(songRepo, lyricsRepo, "Nhất Tư Bách Hài Bất Tự Do",
                    "Ca sĩ 1", "/audio/nhat_tu_bach_hai_bat_tu_do.mp3",
                    "Nhạc Trẻ", "Album Hè 2026", "vi",
                    "/audio/nhat_tu_bach_hai_bat_tu_do.lrc");

            addSong(songRepo, lyricsRepo, "Vân Sơn Ký Tuyết",
                    "Tả Từ", "/audio/van_son_ky_tuyet_ta_tu.mp3",
                    "Bolero", "Tuyển Tập Tả Từ", "vi",
                    "/audio/van_son_ky_tuyet_ta_tu.lrc");

            addSong(songRepo, lyricsRepo, "Cause I Love You",
                    "Nghệ sĩ A", "/audio/causeiloveu.mp3", "Pop",
                    "Single", "en", "/audio/causeiloveu.lrc");

            addSong(songRepo, lyricsRepo, "Dancing With Your Ghost",
                    "Sasha Alex Sloan", "/audio/dancingwithyourghost.mp3",
                    "Pop", "Single", "en",
                    "/audio/dancingwithyourghost.lrc");

            addSong(songRepo, lyricsRepo, "Em Của Ngày Hôm Qua",
                    "Sơn Tùng M-TP", "/audio/emcuangayhomqua.mp3",
                    "V-Pop", "Album 1", "vi",
                    "/audio/emcuangayhomqua.lrc");

            addSong(songRepo, lyricsRepo, "Em Là Cô Dâu Đẹp Nhất",
                    "Nghệ sĩ B", "/audio/emlacodaudepnhat.mp3",
                    "Nhạc Trẻ", "Album 1", "vi",
                    "/audio/emlacodaudepnhat.lrc");

            addSong(songRepo, lyricsRepo, "Monsters", "Katie Sky",
                    "/audio/monsters.mp3", "Pop", "Single", "en",
                    "/audio/monsters.lrc");

            addSong(songRepo, lyricsRepo, "Waiting For Love", "Avicii",
                    "/audio/waitingforlove.mp3", "EDM", "Stories", "en",
                    "/audio/waitingforlove.lrc");

            addSong(songRepo, lyricsRepo, "Ánh Nắng Của Anh", "Đức Phúc",
                    "/audio/anhnangcuaanh.mp3", "Nhạc Trẻ", "OST Chờ Em Đến Ngày Mai", "vi",
                    "/audio/anhnangcuaanh.lrc");

            addSong(songRepo, lyricsRepo, "Shape of You", "Ed Sheeran",
                    "/audio/shapeofyou.mp3", "Pop", "Divide", "en",
                    "/audio/shapeofyou.lrc");

            addSong(songRepo, lyricsRepo, "Ngày Mai Người Ta Lấy Chồng", "Thành Đạt",
                    "/audio/ngaymainguoitalaychong.mp3", "Bolero", "Single", "vi",
                    "/audio/ngaymainguoitalaychong.lrc");

            addSong(songRepo, lyricsRepo, "Faded", "Alan Walker",
                    "/audio/faded.mp3", "EDM", "Different World", "en",
                    "/audio/faded.lrc");

            addSong(songRepo, lyricsRepo, "Nơi Này Có Anh", "Sơn Tùng M-TP",
                    "/audio/noinaycoanh.mp3", "V-Pop", "Single", "vi",
                    "/audio/noinaycoanh.lrc");

            addSong(songRepo, lyricsRepo, "Until I Found You", "Stephen Sanchez",
                    "/audio/untilifoundyou.mp3", "Pop", "Easy On My Eyes", "en",
                    "/audio/untilifoundyou.lrc");

            addSong(songRepo, lyricsRepo, "Bên Trên Tầng Lầu", "Tăng Duy Tân",
                    "/audio/bentrentanglau.mp3", "Nhạc Trẻ", "Single", "vi",
                    "/audio/bentrentanglau.lrc");
        };
    }

    private void addSong(SongRepository songRepo, SongLyricsRepository lyricsRepo,
            String title, String artist, String url, String genre, String album,
            String lang, String lrcUrl) {
        Song s = songRepo.save(new Song(title, artist, url, genre, album));

        SongLyrics sl = new SongLyrics();
        sl.setSong(s);
        sl.setLanguage(lang);
        sl.setFormat("LRC"); 

        String lyricContent = generateLyricsForSong(title);
        sl.setContent(lyricContent);

        sl.setFileUrl(lrcUrl);
        lyricsRepo.save(sl);
    }

    // Hàm phụ sinh lời bài hát 
    private String generateLyricsForSong(String title) {
        switch (title) {
            case "Nhất Tư Bách Hài Bất Tự Do":
                return "[00:01.00](Nhạc dạo...)\n" +
                        "[00:08.50]Ta như một con rối trên sân khấu đời\n" +
                        "[00:13.20]Nhất tư bách hài đều chẳng do ta định đoạt\n" +
                        "[00:18.40]Từng sợi tơ siết chặt đôi vai gầy\n" +
                        "[00:23.10]Nụ cười gượng gạo che giấu ngàn giọt lệ rơi.\n" +
                        "[00:28.00]Đêm lạnh lùng nghe tiếng tơ ngân vang\n" +
                        "[00:32.50]Gánh trên vai bao khúc ca bẽ bàng\n" +
                        "[00:37.00]Thế nhân cười chê bước đi vội vàng\n" +
                        "[00:41.20]Ai thấu cho nỗi lòng tan vỡ nát tan\n" +
                        "[00:46.00]Nguyện một mai dứt bỏ hết dây ràng\n" +
                        "[00:51.00]Tìm về nơi gió ngàn tự do phiêu lãng.";

            case "Vân Sơn Ký Tuyết":
                return "[00:01.00](Nhạc dạo...)\n" +
                        "[00:07.20]Gió thổi mây ngàn qua đỉnh núi xa\n" +
                        "[00:12.80]Tuyết rơi phủ trắng lối mòn xưa ta từng qua\n" +
                        "[00:18.50]Bóng ai mờ ảo trong sương khói chiều\n" +
                        "[00:24.00]Để lại nơi đây một mối tình sầu quạnh hiu.\n" +
                        "[00:30.00]Vân sơn đỉnh núi tuyết mờ sương\n" +
                        "[00:35.20]Người đi bỏ lại vạn nẻo sầu thương\n" +
                        "[00:40.10]Khúc độc hành ca vương vấn vấn vương\n" +
                        "[00:45.00]Biết thuở nào nguôi bóng hình người thương\n" +
                        "[00:50.30]Tuyết tan hoa nở lòng chẳng đổi phương\n" +
                        "[00:55.00]Trọn kiếp này đây ôm một tình nồng.";

            case "Cause I Love You":
                return "[00:01.00](Intro nhạc...)\n" +
                        "[00:10.50]Chợt tỉnh giấc giữa đêm thâu đầy vơi\n" +
                        "[00:14.30]Hình bóng em lại ùa về nơi tâm trí anh\n" +
                        "[00:18.20]Baby cause I love you, yes I love you\n" +
                        "[00:22.40]Đừng bắt con tim này phải học cách quên em.\n" +
                        "[00:27.00]Ngày xưa ấy ta bước chung đôi trên đường dài\n" +
                        "[00:31.50]Mà giờ đây chỉ còn mình anh với tương lai\n" +
                        "[00:35.80]Lời hứa năm xưa giờ đã phai mờ rồi\n" +
                        "[00:40.00]Anh khóc vì nhớ em người ơi\n" +
                        "[00:44.20]Vì tình yêu này trao em mãi mãi không đổi thay\n" +
                        "[00:49.00]Dẫu cho mai này hai ta chia hai lối.";

            case "Dancing With Your Ghost":
                return "[00:01.00](Music...)\n" +
                        "[00:04.20]Yelling at the sky, screaming at the world\n" +
                        "[00:09.10]Baby, why'd you go away? I'm still your girl\n" +
                        "[00:13.50]Holding on to hope, holding on to pain\n" +
                        "[00:18.00]I stay up all night, dancing with your ghost.\n" +
                        "[00:22.50]Every room is filled with your sweet memory\n" +
                        "[00:27.10]I can still hear your voice laughing next to me\n" +
                        "[00:31.80]But I open my eyes and there's only space\n" +
                        "[00:36.20]I'm trying to survive without your warm embrace\n" +
                        "[00:41.00]How do I go on when my heart is broken?\n" +
                        "[00:45.50]Just dancing alone with the words left unspoken.";

            case "Em Của Ngày Hôm Qua":
                return "[00:01.00](Intro Sơn Tùng M-TP...)\n" +
                        "[00:14.20]Liệu rằng chia tay trong nước mắt có giải thoát được nhau\n" +
                        "[00:18.50]Khi hai con tim khác nhịp, thôi đừng chờ đợi chi nữa\n" +
                        "[00:22.30]Em đi xa quá, em đi xa anh quá\n" +
                        "[00:25.80]Có biết không nơi đây anh vẫn đứng đợi một giấc mơ.\n" +
                        "[00:30.00]Hãy nhìn về phía trước nơi anh từng đón đưa\n" +
                        "[00:34.20]Cùng những ký ức ngọt ngào ta dưới màn mưa\n" +
                        "[00:38.50]Đừng xóa hết đi tình yêu thương ngày hôm qua\n" +
                        "[00:42.10]Và đừng ngoảnh mặt bước đi lạnh lùng thế mà\n" +
                        "[00:46.00]Gió cuốn bay câu thề, người thương giờ vội quên lối về\n" +
                        "[00:51.20]Trả lại cho anh bình yên như lúc chưa bắt đầu.";

            case "Em Là Cô Dâu Đẹp Nhất":
                return "[00:01.00](Nhạc dạo đám cưới...)\n" +
                        "[00:09.30]Ngày hôm nay em khoác lên mình chiếc váy cưới trắng tinh khôi\n" +
                        "[00:14.50]Thẹn thùng bước bên anh trước sự chứng kiến của bao người\n" +
                        "[00:19.20]Từ nay về sau, dẫu vui hay buồn, dẫu gian nan\n" +
                        "[00:24.60]Nắm chặt tay nhau, ta đi đến tận cùng hạnh phúc.\n" +
                        "[00:29.50]Tiếng pháo vang rộn ràng vang khắp cả xóm làng\n" +
                        "[00:34.00]Chúc phúc cho duyên tình ta mãi mãi nồng nàn\n" +
                        "[00:38.20]Cám ơn cuộc đời đã mang em đến bên anh\n" +
                        "[00:43.00]Vẽ nên bức tranh tình yêu ngập tràn sắc xanh\n" +
                        "[00:47.50]Hãy tự tin em nhé, vì từ nay bên cạnh em\n" +
                        "[00:52.00]Sẽ luôn có một vòng tay che chở dịu êm.";

            case "Monsters":
                return "[00:01.00](Intro Katie Sky...)\n" +
                        "[00:05.50]I see your monsters, I see your pain\n" +
                        "[00:09.80]Tell me your problems, I'll chase them away\n" +
                        "[00:14.20]I'll be your lighthouse when you're lost at sea\n" +
                        "[00:18.50]Keep my word and I'll be your shield.\n" +
                        "[00:23.00]You don't have to hide the tears in your eyes\n" +
                        "[00:27.50]I'll stand by your side through all of the lies\n" +
                        "[00:32.00]When the shadows fall and the night gets cold\n" +
                        "[00:36.40]I have a hand for you to tightly hold\n" +
                        "[00:41.00]We will face the dark, we will win the fight\n" +
                        "[00:45.50]And bring your soul back into the beautiful light.";

            case "Waiting For Love":
                return "[00:01.00](Intro EDM...)\n" +
                        "[00:15.00]Where there's a will, there's a way, kind of beautiful\n" +
                        "[00:19.50]And every night has its day, so magical\n" +
                        "[00:24.00]And if there's love in this life, there's no obstacle\n" +
                        "[00:28.20]That can't be defeated\n" +
                        "[00:32.00]For every tyrant a tear for the vulnerable\n" +
                        "[00:36.50]In every lost soul the bones of a miracle\n" +
                        "[00:41.00]For every dreamer a dream we're unstoppable\n" +
                        "[00:45.00]With something to believe in\n" +
                        "[00:49.00]Monday left me broken, Tuesday I was through with hoping\n" +
                        "[00:54.00]Wednesday my empty arms were open, Thursday waiting for love.";

            case "Ánh Nắng Của Anh":
                return "[00:01.00](Piano Intro...)\n" +
                        "[00:12.30]Từ bao lâu nay anh cứ mãi cô đơn bơ vơ\n" +
                        "[00:17.40]Bao giai điệu quen thuộc vẫn ngân lên trong giấc mơ\n" +
                        "[00:22.50]Rồi em bước đến như ánh ban mai rạng ngời\n" +
                        "[00:27.10]Xua tan đi màn đêm tăm tối trong cuộc đời\n" +
                        "[00:32.00]Lại gần bên anh và hãy tựa vào vai anh nhé em\n" +
                        "[00:37.20]Để anh che chở cho em qua những giông bão ngoài kia\n" +
                        "[00:42.50]Vì em chính là ánh nắng ấm áp của đời anh\n" +
                        "[00:47.00]Nguyện bên em đi qua muôn ngàn trùng xanh.";

            case "Shape of You":
                return "[00:01.00](Marimba Intro...)\n" +
                        "[00:05.20]The club isn't the best place to find a lover\n" +
                        "[00:08.50]So the bar is where I go\n" +
                        "[00:11.20]Me and my friends at the table doing shots\n" +
                        "[00:14.00]Drinking fast and then we talk slow\n" +
                        "[00:16.50]Come over and start up a conversation with just me\n" +
                        "[00:19.50]And trust me I'll give it a chance now\n" +
                        "[00:22.00]Take my hand, stop, put Van the Man on the jukebox\n" +
                        "[00:25.10]And then we start to dance\n" +
                        "[00:27.00]I'm in love with the shape of you\n" +
                        "[00:29.80]We push and pull like a magnet do.";

            case "Ngày Mai Người Ta Lấy Chồng":
                return "[00:01.00](Nhạc dạo Acoustic...)\n" +
                        "[00:10.50]Tiếng đàn hò hẹn ngày xưa nay bỗng sao nghe nghẹn ngào\n" +
                        "[00:16.20]Bến sông đò ngang giờ đây vắng bóng ai thuở nao\n" +
                        "[00:21.80]Người đi rộn ràng pháo đỏ rượu nồng kiệu hoa sang ngang\n" +
                        "[00:27.50]Bỏ lại nơi đây một gã si tình lệ tuôn chứa chan\n" +
                        "[00:33.00]Ngày mai người ta lấy chồng rồi người ơi có biết không\n" +
                        "[00:38.20]Đêm nay mình anh ôm nỗi sầu đau thấu tận cõi lòng\n" +
                        "[00:44.00]Trách cho duyên phận mình nghèo chẳng giữ được tay em\n" +
                        "[00:49.50]Nhìn người hạnh phúc bên ai lòng anh nát tan.";

            case "Faded":
                return "[00:01.00](Intro Synth...)\n" +
                        "[00:06.00]You were the shadow to my light, did you feel us?\n" +
                        "[00:12.50]Another start, you fade away\n" +
                        "[00:18.00]Afraid our aim is out of sight, wanna see us\n" +
                        "[00:24.20]Alight\n" +
                        "[00:27.00]Where are you now? Where are you now?\n" +
                        "[00:33.00]Where are you now? Was it all in my fantasy?\n" +
                        "[00:39.20]Where are you now? Were you only imaginary?\n" +
                        "[00:45.00]Where are you now? Atlantis, under the sea\n" +
                        "[00:52.00]Under the sea, where are you now?\n" +
                        "[00:57.00]Another dream, the monster's running wild inside of me\n" +
                        "[01:03.00]I'm faded, I'm faded, so lost, I'm faded.";

            case "Nơi Này Có Anh":
                return "[00:01.00](Intro vui tươi...)\n" +
                        "[00:11.40]Bao nhiêu thương nhớ gửi vào làn gió mát lành đầu đông\n" +
                        "[00:16.50]Bao nhiêu yêu dấu gói gọn vào trong trái tim hồng\n" +
                        "[00:21.20]Cầm tay em bước đi trên con đường ngập tràn tia nắng\n" +
                        "[00:26.00]Nụ cười em rạng rỡ làm say đắm cả không gian\n" +
                        "[00:31.00]Bởi vì nơi này có em, bình yên quay về bên anh\n" +
                        "[00:36.20]Xua đi bao nhiêu lo âu muộn phiền tháng ngày mong manh\n" +
                        "[00:41.50]Hãy hứa bên anh trọn đời, dẫu mai vật đổi sao dời\n" +
                        "[00:46.00]Tình yêu ta trao nhau mãi luôn sáng ngời.";

            case "Until I Found You":
                return "[00:01.00](Guitar 50s style Intro...)\n" +
                        "[00:08.20]Georgia, wrap me up in all your\n" +
                        "[00:13.50]I want you in my arms, oh, let me hold you\n" +
                        "[00:19.00]I'll never let you go again like I did\n" +
                        "[00:24.20]Oh, I used to say\n" +
                        "[00:28.00]I would never fall in love until I found her\n" +
                        "[00:33.10]I said, I would never fall in love until I found her\n" +
                        "[00:38.50]And I was broke, heart was bruised\n" +
                        "[00:43.00]Until I found you, until I found you\n" +
                        "[00:48.00]Now she's in my arms, and I'm safe.";

            case "Bên Trên Tầng Lầu":
                return "[00:01.00](Deep Bass Intro...)\n" +
                        "[00:07.50]Em ơi đừng khóc bóng tối trước mắt sẽ bắt em đi\n" +
                        "[00:11.20]Em ơi đừng lo em ơi đừng tiếc nuối làm gì\n" +
                        "[00:15.00]Yêu thương một ai mà mang về toàn nỗi đau\n" +
                        "[00:18.50]Thì buông tay ra cho con tim thôi bớt u sầu\n" +
                        "[00:22.00]Vì sao em phải khóc? Có đáng để buồn thế không?\n" +
                        "[00:25.80]Nỗi đau thương này có giúp em mạnh mẽ hơn không?\n" +
                        "[00:29.50]Bước xuống bên trên tầng lầu, gạt đi giọt nước mắt rơi\n" +
                        "[00:33.20]Tìm lại ánh sáng tươi đẹp phía chân trời.";
            default:
                return "[00:01.00](Giai điệu đang phát...)\n" +
                        "[00:05.00]Hệ thống đang cập nhật thêm lời cho bài hát " + title + ".";
        }
    }
}