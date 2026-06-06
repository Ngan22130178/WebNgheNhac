package vn.edu.nlu.fit.musicweb.controller;

import vn.edu.nlu.fit.musicweb.model.PlaylistAlbum;
import vn.edu.nlu.fit.musicweb.repository.PlaylistAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal")
public class PersonalController {

    @Autowired
    private PlaylistAlbumRepository playlistAlbumRepository;

    @GetMapping("/playlists")
    public ResponseEntity<ResponseDTO<List<PlaylistAlbum>>> getMyPlaylists(@RequestParam("userId") String userId) {
        List<PlaylistAlbum> playlists = playlistAlbumRepository.findByOwnerIdAndType(userId, "PLAYLIST");
        ResponseDTO<List<PlaylistAlbum>> response = new ResponseDTO<>(true, "Tải danh sách Playlist thành công!", playlists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/albums")
    public ResponseEntity<ResponseDTO<List<PlaylistAlbum>>> getMyAlbums(@RequestParam("userId") String userId) {
        List<PlaylistAlbum> albums = playlistAlbumRepository.findByOwnerIdAndType(userId, "ALBUM");
        ResponseDTO<List<PlaylistAlbum>> response = new ResponseDTO<>(true, "Tải danh sách Album thành công!", albums);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorites")
    public ResponseEntity<ResponseDTO<List<PlaylistAlbum>>> getMyFavorites(@RequestParam("userId") String userId) {
        List<PlaylistAlbum> favorites = playlistAlbumRepository.findByOwnerIdAndType(userId, "FAVORITE");
        ResponseDTO<List<PlaylistAlbum>> response = new ResponseDTO<>(true, "Tải danh sách bài hát yêu thích thành công!", favorites);
        return ResponseEntity.ok(response);
    }
}