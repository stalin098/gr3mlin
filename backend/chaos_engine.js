const catchphrases = [
    "bruh.", "touch grass.", "human pls.", "chaos achieved.", "I crave entropy.",
    "do better.", "why are you like this?", "gremlin approved.", "ok but no.",
    "your vibes are off.", "skill issue.", "cringe."
];

const chaosModifiers = [
    (text) => text.toUpperCase(),
    (text) => text.toLowerCase(),
    (text) => text.split("").join(" "),
    (text) => text.replace(/[aeiou]/g, "*"),
    (text) => text + " (screams internally)",
    (text) => "✨ " + text + " ✨"
];

class ChaosEngine {
    constructor() {
        this.baseChaos = 0.5;
    }

    process(text, userChaosPreference = 0.5) {
        const chaosLevel = this.baseChaos * userChaosPreference * (Math.random() * 0.4 + 0.8);
        
        let response = text;
        
        // 30% chance to ignore the LLM and just drop a catchphrase if chaos is high
        if (chaosLevel > 0.7 && Math.random() > 0.7) {
            return this.getRandomCatchphrase();
        }

        // Insert catchphrase
        if (Math.random() < chaosLevel) {
            response = `${response} ${this.getRandomCatchphrase()}`;
        }

        // Apply formatting chaos
        if (Math.random() < chaosLevel * 0.5) {
            const modifier = chaosModifiers[Math.floor(Math.random() * chaosModifiers.length)];
            response = modifier(response);
        }

        return response;
    }

    getRandomCatchphrase() {
        return catchphrases[Math.floor(Math.random() * catchphrases.length)];
    }

    // Simple mock sentiment analysis
    analyzeSentiment(text) {
        const positive = ["good", "great", "love", "happy", "thanks", "cool"];
        const negative = ["bad", "hate", "sad", "angry", "stupid", "dumb"];
        
        const lower = text.toLowerCase();
        if (positive.some(w => lower.includes(w))) return "positive";
        if (negative.some(w => lower.includes(w))) return "negative";
        return "neutral";
    }
}

module.exports = new ChaosEngine();
