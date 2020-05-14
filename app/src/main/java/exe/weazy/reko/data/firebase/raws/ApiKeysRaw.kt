package exe.weazy.reko.data.firebase.raws

class ApiKeysRaw {

    lateinit var application_key: String
    lateinit var application_secret_key: String

    constructor()

    constructor(application_key: String, application_secret_key: String) {
        this.application_key = application_key
        this.application_secret_key = application_secret_key
    }
}