package Data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtrackerapp.R

class RecyclerViewAdapter(val list: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val sr = view.findViewById<TextView>(R.id.serial_number)
        val state = view.findViewById<TextView>(R.id.state)
        val count  = view.findViewById<TextView>(R.id.count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater= LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]
        Log.e(item,":")
        val items = item.split(',')
           holder.sr.text = items[0]
           holder.state.text= items[1]
           holder.count.text = items[2]
    }

    override fun getItemCount(): Int {
      return list.size
    }
}