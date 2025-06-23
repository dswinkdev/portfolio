const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const app = express();

app.use(cors());
app.use(express.json());

mongoose.connect('mongodb://localhost:27017/eventrack')
  .then(() => console.log('MongoDB Connected'))
  .catch(err => console.error('Connection error:', err));

const eventRoutes = require('./routes/events');
app.use('/api/events', eventRoutes);

const PORT = 5000;
app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));
