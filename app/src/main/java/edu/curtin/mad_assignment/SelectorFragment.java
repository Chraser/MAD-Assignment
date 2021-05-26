package edu.curtin.mad_assignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class SelectorFragment extends Fragment {
    private Structure currentStruct;
    private SelectorAdapter adapter;

    public SelectorFragment() {
        currentStruct = StructureData.get().get(0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selector, ui, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.selectorRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        StructureData data = StructureData.get();
        adapter = new SelectorAdapter(data);
        rv.setAdapter(adapter);
        return view;
    }

    public Structure getCurrentStruct()
    {
        return currentStruct;
    }

    private class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder>
    {
        private StructureData data;

        public SelectorAdapter(StructureData data)
        {
            this.data = data;
        }

        @Override
        public int getItemCount()
        {
            return data.size();
        }

        @NonNull
        @Override
        public SelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity()); // <-- Fragment method
            return new SelectorViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectorViewHolder vh, int index)
        {
            vh.bind(data.get(index));
        }
    }

    private class SelectorViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView structure;
        private TextView label;
        private Structure currentData;

        public SelectorViewHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.list_selection,parent, false));
            structure = (ImageView)itemView.findViewById(R.id.list_image);
            label = (TextView)itemView.findViewById(R.id.list_data);
            //make the entire ViewHolder clickable since the structure image may be too small to click on sometimes
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    currentStruct = currentData;
                    GameData gameData = GameData.get();
                    gameData.setBuildMode();
                    adapter.notifyItemChanged(getAdapterPosition());
                }
            });
        }

        public void bind (Structure data)
        {
            structure.setImageResource(data.getDrawableId());
            label.setText(data.getLabel());
            currentData = data;
        }
    }
}
