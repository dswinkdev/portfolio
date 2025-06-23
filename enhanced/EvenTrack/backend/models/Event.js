const mongoose = require('mongoose');

const EventSchema = new mongoose.Schema({
  name: String,
  date: String,
  location: String
});

module.exports = mongoose.model('Event', EventSchema);
