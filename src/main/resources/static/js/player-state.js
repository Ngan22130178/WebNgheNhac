// ==========================================
// 1. TRẠNG THÁI & BIẾN TOÀN CỤC
// ==========================================
let currentPlayingId = null;
let queue = [];
let currentIndex = -1;
let allSongs = []; // Nơi lưu trữ toàn bộ danh sách nhạc của hệ thống
let loopMode = 0; // 0: Off, 1: Loop 1, 2: Loop All, 3: Shuffle
let isShuffled = false; // Biến trạng thái
let originalQueue = []; // Lưu trữ thứ tự gốc khi chưa trộn

const player = document.getElementById('mainPlayer');

