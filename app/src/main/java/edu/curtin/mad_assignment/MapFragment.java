package edu.curtin.mad_assignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MapFragment extends Fragment {


    private SelectorFragment selectorFrag;
    private MapAdapter adapter;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_map, ui, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.mapRecyclerView);
        GameData gameData = GameData.get();
        Settings settings = Settings.get();
        rv.setLayoutManager(new GridLayoutManager(getActivity(), settings.getMapHeight(), GridLayoutManager.HORIZONTAL, false));
        adapter = new MapAdapter(gameData, settings);
        rv.setAdapter(adapter);
        return view;
    }

    public void notifyAdapter(int position)
    {
        adapter.notifyItemChanged(position);
    }

    public void setSelectorFrag(SelectorFragment frag)
    {
        selectorFrag = frag;
    }

    private class MapAdapter extends RecyclerView.Adapter<MapViewHolder>
    {
        private GameData data;
        private Settings settings;

        public MapAdapter(GameData data, Settings settings)
        {
            this.data = data;
            this.settings = settings;
        }

        @Override
        public int getItemCount()
        {
            return settings.getMapHeight() * settings.getMapWidth();
        }

        @Override
        public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity()); // <-- Fragment method
            return new MapViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(MapViewHolder vh, int index)
        {
            int row = index % settings.getMapHeight();
            int col = index / settings.getMapHeight();
            vh.bind(data.get(row,col));
        }
    }

    private class MapViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView northEast, northWest, southEast, southWest, structure;
        private MapElement element;
        public MapViewHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.grid_cell,parent, false));
            int size = parent.getMeasuredHeight() / Settings.get().getMapHeight() + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;
            northEast = (ImageView)itemView.findViewById(R.id.northEast);
            northWest = (ImageView)itemView.findViewById(R.id.northWest);
            southEast = (ImageView)itemView.findViewById(R.id.southEast);
            southWest = (ImageView)itemView.findViewById(R.id.southWest);
            structure = (ImageView)itemView.findViewById(R.id.structure);
            structure.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    TextView money = getActivity().findViewById(R.id.currentMoneyText);
                    TextView population = getActivity().findViewById(R.id.populationText);
                    TextView employmentRate = getActivity().findViewById(R.id.employmentRateText);
                    GameData gameData = GameData.get();
                    int mode = gameData.getCurrentMode();

                    //do build operation if mode is build mode
                    if(mode == GameData.BUILD_MODE) {
                        //check if structure is null before building
                        if (element.getStructure() == null)
                        {
                            Structure structData = selectorFrag.getCurrentStruct();
                            //using instanceof operator to check which structure type it is to know how much to pay for and update the respective ui elements
                            if(structData instanceof Residential)
                            {
                                if(gameData.isNextToRoad(element.getRow(), element.getCol())) {
                                    if (gameData.payForResidential()) {
                                        element.setStructure(selectorFrag.getCurrentStruct());
                                        money.setText(Integer.toString(gameData.getMoney()));
                                        population.setText(Integer.toString(gameData.getPopulation()));
                                        employmentRate.setText((Integer.toString((int) (gameData.getEmploymentRate() * 100.0))) + "%");
                                    }
                                }
                            }
                            else if(structData instanceof Commercial) {
                                if (gameData.isNextToRoad(element.getRow(), element.getCol()))
                                {
                                    if (gameData.payForCommercial()) {
                                        element.setStructure(selectorFrag.getCurrentStruct());
                                        money.setText(Integer.toString(gameData.getMoney()));
                                        employmentRate.setText((Integer.toString((int) (gameData.getEmploymentRate() * 100.0))) + "%");
                                    }
                                }
                            }
                            else if(gameData.payForRoad())
                            {
                                element.setStructure(selectorFrag.getCurrentStruct());
                                money.setText(Integer.toString(gameData.getMoney()));
                            }
                            adapter.notifyItemChanged(getAdapterPosition());
                        }
                    }
                    //demolish mode is selected
                    else if(mode == GameData.DEMOLISH_MODE)
                    {
                        //decrement number of residential if structure is residential type
                        if(element.getStructure() instanceof Residential)
                        {
                            gameData.decrementNResidential();
                            population.setText(Integer.toString(gameData.getPopulation()));
                        }
                        //decrement number of commercial if structure is commercial type
                        else if(element.getStructure() instanceof Commercial)
                        {
                            gameData.decrementNCommericial();
                            employmentRate.setText((Integer.toString((int)Math.round(gameData.getEmploymentRate() * 100.0))) + "%");
                        }
                        element.removeStructure();
                        adapter.notifyItemChanged(getAdapterPosition());
                    }
                    else
                    {
                        //details mode selected
                        if(element.getStructure() != null) {
                            Intent intent = DetailsActivity.getIntent(getActivity(), getAdapterPosition(), element.getRow(), element.getCol(),
                                                                      element.getOwnerName(), element.getStructure().getLabel(),
                                                                      element.getStructure().getDrawableId(), element.getImage());
                            getActivity().startActivityForResult(intent, MapActivity.REQUEST_DETAILS);
                        }
                    }
                }
            });
        }

        public void bind (MapElement data)
        {
            northEast.setImageResource(data.getNorthEast());
            northWest.setImageResource(data.getNorthWest());
            southEast.setImageResource(data.getSouthEast());
            southWest.setImageResource(data.getSouthWest());
            if(data.getImage() != null)
            {
                structure.setImageBitmap(data.getImage());
            }
            //only use structure drawable id if no image exist
            else if (data.getStructure() != null)
            {
                structure.setImageResource(data.getStructure().getDrawableId());
            }
            //use transparent image if no structure or image
            else
            {
                structure.setImageResource(0);
            }
            element = data;
        }
    }
}
