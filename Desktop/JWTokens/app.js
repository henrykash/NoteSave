const express = require('express');
const jwt = require('jsonwebtoken');

const app = express();

app.get('/api',(req, res)=> {
    res.json({
        message: 'Welcome to API'
    });
});

app.post('/api/posts',(req, res)=> {
    res.json({
        message: 'Post created'
    });
});

app.post('/api/login',(req, res)=> {
    res.json({
        message: 'Welcome to API'
    });
});
app.listen(5000, () => console.log('server running on port 5000'));
