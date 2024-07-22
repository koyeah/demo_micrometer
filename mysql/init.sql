CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
);

INSERT INTO books (title, author) VALUES 
('To Kill a Mockingbird', 'Harper Lee'),
('1984', 'George Orwell'),
('Pride and Prejudice', 'Jane Austen');