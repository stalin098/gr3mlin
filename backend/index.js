require('dotenv').config();
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const chaosEngine = require('./chaos_engine');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(bodyParser.json());

// Mock Database
const db = {
    logs: [],
    metrics: {
        totalMessages: 0,
        totalChaos: 0,
        startTime: Date.now()
    }
};

// --- ROUTES ---

app.post('/chat', async (req, res) => {
    const { message, chaosPreference } = req.body;

    // 1. Log input
    db.metrics.totalMessages++;

    // 2. Mock LLM Call (Replace with actual OpenAI/Gemini call if keys provided)
    // For now, we simulate a "smart" response based on keywords or just echo with sass.
    let llmResponse = `You said "${message}". Fascinating.`;

    if (message.toLowerCase().includes("hello")) llmResponse = "Oh, it's you.";
    if (message.toLowerCase().includes("help")) llmResponse = "Figure it out yourself.";
    if (message.toLowerCase().includes("joke")) llmResponse = "Your life.";

    // 3. Apply Chaos
    const chaoticResponse = chaosEngine.process(llmResponse, chaosPreference || 0.5);

    // 4. Log interaction
    db.logs.push({
        timestamp: Date.now(),
        user: message,
        gremlin: chaoticResponse,
        chaos: chaosPreference
    });

    res.json({
        response: chaoticResponse,
        sentiment: chaosEngine.analyzeSentiment(message)
    });
});

app.post('/quick-action', (req, res) => {
    const { action } = req.body;
    let response = "";

    switch (action) {
        case "insult":
            response = "You look like a glitched NPC.";
            break;
        case "motivate":
            response = "Do it or don't. I don't care. (Do it though)";
            break;
        case "chaos":
            response = chaosEngine.process("PURE CHAOS MODE ACTIVATED", 1.0);
            break;
        case "vibe":
            response = "Vibe check: FAILED.";
            break;
        default:
            response = "Unknown command. Typical.";
    }

    res.json({ response });
});

app.get('/metrics', (req, res) => {
    res.json({
        uptime: (Date.now() - db.metrics.startTime) / 1000,
        totalMessages: db.metrics.totalMessages,
        logs: db.logs.slice(-10) // Last 10 logs
    });
});

app.listen(PORT, () => {
    console.log(`Gremlin Backend running on port ${PORT}`);
    console.log(`Chaos Engine initialized.`);
});
