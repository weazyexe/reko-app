package exe.weazy.reko.data.firebase.raws

class EmotionRaw {

    lateinit var name: String
    var value: Int = 0

    constructor()

    constructor(name: String, value: Int) {
        this.name = name
        this.value = value
    }
}