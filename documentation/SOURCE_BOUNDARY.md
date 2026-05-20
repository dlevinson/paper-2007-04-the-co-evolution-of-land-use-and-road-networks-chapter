# Source Boundary

## Included

The included archive assets are the final/reference paper copy, the SIGNAL Java source, compact runnable wrapper files from the 2021 update, and model inputs/configuration needed to run or reconstruct the simulations.

The source package includes `.java` files, the simple applet HTML file, Java policy metadata, Eclipse project metadata, grid networks, coordinate files, TAZ information, OD-cost input, river/grid variants, and the SIGNAL batch configuration.

## Excluded

The local ISTTT paper folder contains drafts, formatted versions, contributor agreements, conference program/travel PDFs, presentations, Keynote bundles, and figure/image exports. These are not data/code assets and should not be carried into a public GitHub archive for this paper.

The older `SONG1-Code` and `NDApplication` files are not included here. They are relevant predecessor/background assets and are already associated with the earlier network hierarchy package, but the `Signal1.8.1` folder is the tighter match to this co-evolution chapter.

The `signal1.8.0-*.txt` files in the SIGNAL folder are excluded because they are large generated outputs rather than required inputs. The staged inputs and code are sufficient to represent the reproducibility package boundary without copying every generated run.

Compiled `.class` files from the 2006 source tree are excluded because the `.java` source is included. The 2021 runnable jar/exe wrapper is included separately as a compact usability aid.
