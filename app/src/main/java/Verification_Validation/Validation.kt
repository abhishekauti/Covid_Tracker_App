package Verification_Validation

class Validation {
    fun verify_name( name:String):Boolean
    {
        if(name.length<6) {

            return false
        }
        //  var i =0
        for(i in 0..name.length-1)
        {
            var c = name[i]
            c = c.toLowerCase()
            if((c.toInt() < 97 && c.toInt()!=32) || c.toInt()>122)
                return false
        }
        return true
    }
    fun verify_password(pass:String):Boolean
    {
        if(pass.length<6)
            return false
        var ischar = false
        var isnum = false
        var iscaps = false
        var issmall=false
        for(i in 0..pass.length-1)
        {
            val c = pass[i].toInt()
            if(c>47 && c<56)
                isnum=true
            else if(c>64 && c<91)
                iscaps=true
            else if(c>97 && c<122)
                issmall=true
            else if(c!=32)
                ischar=true
        }
        return ischar && isnum && issmall && iscaps
    }

    fun verify_email(userEmail: String): Boolean {
        var isnum = false
        var isspecial = false
        if(userEmail.length<10)return false
        for(i in 0.. userEmail.length-1)
        {

            val c= userEmail[i]
            if(c == '@')
                isspecial=true
            val d=c.toInt()
            if(d in 48..56)
                isnum = true
        }
        return isnum && isspecial
    }
}